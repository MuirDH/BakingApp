package com.dragonnedevelopment.bakingapp.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dragonnedevelopment.bakingapp.R;
import com.dragonnedevelopment.bakingapp.models.Recipe;
import com.dragonnedevelopment.bakingapp.models.Step;
import com.dragonnedevelopment.bakingapp.utils.Config;
import com.dragonnedevelopment.bakingapp.utils.Utils;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * BakingApp Created by Muir on 30/04/2018.
 */
public class RecipeStepDetailFragment extends Fragment implements Player.EventListener {

    private static final String TAG = RecipeStepDetailFragment.class.getSimpleName();

    private Unbinder unbinder;
    private Context context;

    private Recipe selectedRecipe;
    private Step selectedStep;
    private int stepId;
    private int stepCount;
    private String stepDesc;
    private String videoUrl;
    private String imageUrl;
    private SimpleExoPlayer exoPlayer;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;
    private BandwidthMeter bandwidthMeter;
    private TrackSelector trackSelector;
    private long playerPosition;

    //Butterknife View binding
    @BindView(R.id.textview_step_description)
    TextView textViewStepDescription;

    @BindView(R.id.rlayout_player)
    RelativeLayout relativeLayoutPlayer;

    @BindView(R.id.playerview_recipe_video)
    SimpleExoPlayerView exoPlayerView;

    @BindView(R.id.imageview_no_media)
    ImageView imageViewNoMedia;

    //Butterknive Resource binding
    @BindDrawable(R.drawable.logo)
    Drawable recipeDefaultImage;

    /**
     * Empty constructor
     */
    public RecipeStepDetailFragment() {

    }

    /**
     * Override this method to get the context method, as the fragment is used by multiple activities
     *
     * @param context the context method
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            ArrayList<Recipe> recipeArrayList =
                    getArguments().getParcelableArrayList(Config.INTENT_KEY_SELECTED_RECIPE);

            if (recipeArrayList != null) {
                selectedRecipe = recipeArrayList.get(0);
                stepId = getArguments().getInt(Config.INTENT_KEY_SELECTED_STEP);
                stepCount = getArguments().getInt(Config.INTENT_KEY_STEP_COUNT);

                getStepDetails();
            }
        }

        if (savedInstanceState != null)
            playerPosition = savedInstanceState.getLong(Config.STATE_PLAYER_POSITION);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_step_details, container,
                false);
        unbinder = ButterKnife.bind(this, rootView);

        // create exoplayer to show recipe video
        if (savedInstanceState == null) createMediaPlayer();

        // display steps description
        displayStepsData();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(Config.STATE_PLAYER_POSITION, playerPosition);
    }

    /**
     * get the description and video for the selected step
     */
    public void getStepDetails() {
        selectedStep = selectedRecipe.getRecipeSteps().get(stepId);
        stepDesc = selectedStep.getStepDescription();
        videoUrl = selectedStep.getStepVideoUrl();
        imageUrl = selectedStep.getStepThumbnailUrl();
    }

    /**
     * display the step details
     */
    private void displayStepsData() {
        // remove the step number from the step description (sample raw data >= 1.
        if (stepId > 0) {
            int index = stepDesc.indexOf(". ");
            stepDesc = stepDesc.substring(index + 2);
        }
        textViewStepDescription.setText(stepDesc);
    }

    /**
     * create the Exoplayer instance and attach media to it
     */
    public void createMediaPlayer() {
        if (!Utils.isEmptyString(videoUrl)) {
            // hide the overlay default image
            ButterKnife.apply(imageViewNoMedia, Utils.VISIBILITY, View.GONE);

            initialiseMediaSession();
            initialisePlayer(Uri.parse(videoUrl));

        } else {
            /*
             * The step does not have a video, so show the recipe image instead.
             * If no recipe image is found, display a default image
             */
            ButterKnife.apply(imageViewNoMedia, Utils.VISIBILITY, View.VISIBLE);

            if (!Utils.isEmptyString(imageUrl)) Picasso.with(context).load(imageUrl)
                    .placeholder(recipeDefaultImage)
                    .error(recipeDefaultImage)
                    .into(imageViewNoMedia);
            else imageViewNoMedia.setImageDrawable(recipeDefaultImage);
        }
    }

    /**
     * Initialise the media session to be enabled with media buttons, transport controls, callbacks,
     * and media controller
     */
    private void initialiseMediaSession() {
        //create a MediaSessionCompat
        mediaSession = new MediaSessionCompat(context, TAG);

        // enable callbacks from MediaButtons and TransportControls
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        );

        // stop MediaButtons from restarting the player when the app is not visible
        mediaSession.setMediaButtonReceiver(null);

        // set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
        stateBuilder = new PlaybackStateCompat.Builder().setActions(
                PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE
        );

        mediaSession.setPlaybackState(stateBuilder.build());

        // MediaSessionCallback has methods that handle callbacks from a media controller
        mediaSession.setCallback(new MediaSessionCallback());

        // The activity is active so start the MediaSession
        mediaSession.setActive(true);
    }

    /**
     * Initialise the player
     *
     * @param mediaUri - the uri of the media to be played
     */
    private void initialisePlayer(Uri mediaUri) {
        if (exoPlayer == null) {
            // create the player instance
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            // create the player
            exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

            // attach player to the view
            exoPlayerView.setPlayer(exoPlayer);

            // Prepare the player

            // bandwidthMeter measures bandwidth during playback. can be null if not required
            bandwidthMeter = new DefaultBandwidthMeter();

            // create datasource instance through which media is loaded
            String userAgent = Util.getUserAgent(context, TAG);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, userAgent);
            MediaSource mediaSource =
                    new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
            exoPlayer.prepare(mediaSource);

            exoPlayer.setPlayWhenReady(true);
            exoPlayer.seekTo(playerPosition);
        }
    }

    /**
     * Media Session Callbacks enables all external clients to control the player
     */
    private class MediaSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            super.onPlay();
            exoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            super.onPause();
            exoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            exoPlayer.seekTo(0);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayer != null) playerPosition = exoPlayer.getCurrentPosition();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || exoPlayer == null)) createMediaPlayer();
    }

    /**
     * release the player and deactivate the media session
     */
    private void releasePlayer() {
        if (exoPlayer != null) {
            playerPosition = exoPlayer.getCurrentPosition();

            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }

        if (mediaSession != null) mediaSession.setActive(false);

        if (trackSelector != null) trackSelector = null;
    }
}
