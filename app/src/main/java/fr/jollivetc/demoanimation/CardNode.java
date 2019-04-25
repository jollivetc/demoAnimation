package fr.jollivetc.demoanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.QuaternionEvaluator;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.math.Vector3Evaluator;

public class CardNode extends Node implements Node.OnTapListener {

    private boolean isFocused;
    private AnchorNode anchorNode;
    private ObjectAnimator positionAnimator;
    private ObjectAnimator orientationAnimator;

    public CardNode(AnchorNode anchorNode){
        this.anchorNode = anchorNode;
        isFocused = false;
        setOnTapListener(this);
        this.setParent(anchorNode);
    }


    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);
        if(positionAnimator != null){
            this.setWorldPosition((Vector3) positionAnimator.getAnimatedValue());
        }
        if(orientationAnimator != null){
            this.setWorldRotation((Quaternion)orientationAnimator.getAnimatedValue());
        }
    }

    @Override
    public void onTap(HitTestResult hitTestResult, MotionEvent motionEvent) {
        if (isFocused){
            prepareAnimationsToSendCardToAnchor();
        }else{
            prepareAnimationsToBringCardToFront();
        }
        positionAnimator.start();
        orientationAnimator.start();
        this.isFocused = !this.isFocused;

    }

    private void prepareAnimationsToBringCardToFront() {
        //Create a temporary node at destination (1 meter in front of camera) with orientation
        final Node temporaryDestinationNode = new Node();
        setNode1MeterInFrontCamera(temporaryDestinationNode);
        //compute start and end position
        buildAnimators(temporaryDestinationNode);
        //clean temporary node
        temporaryDestinationNode.setParent(null);
        //at the end of animation, attach the card in front of the camera
        positionAnimator.addListener(createFocusAnimationEndListener());
    }

    private void prepareAnimationsToSendCardToAnchor() {
        // create a temporary node at destination (at anchorNode) with orientation (laying flat)
        final Node temporaryDestinationNode = new Node();
        setNodeAtAnchor(temporaryDestinationNode);
        //build animators
        buildAnimators(temporaryDestinationNode);
        //clean temporary node
        temporaryDestinationNode.setParent(null);
        //at the end of animation, attach the card to the anchorNode.
        positionAnimator.addListener(createReturnAnimationEndListener());
    }

    private AnimatorListenerAdapter createFocusAnimationEndListener() {
        return new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setNode1MeterInFrontCamera(CardNode.this);
                positionAnimator = null;
                orientationAnimator = null;
            }
        };
    }

    private AnimatorListenerAdapter createReturnAnimationEndListener() {
        return new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setNodeAtAnchor(CardNode.this);
                positionAnimator = null;
                orientationAnimator = null;
            }
        };
    }

    private void setNodeAtAnchor(Node temporaryDestinationNode) {
        temporaryDestinationNode.setParent(anchorNode);
        temporaryDestinationNode.setLocalPosition(new Vector3(0f, 0f, 0f));
        temporaryDestinationNode.setLocalRotation(new Quaternion(new Vector3(-90f, 0f, 0f)));
    }

    private void setNode1MeterInFrontCamera(Node temporaryDestinationNode) {
        temporaryDestinationNode.setParent(getScene().getCamera());
        temporaryDestinationNode.setLocalPosition(new Vector3(0.0f, 0.0f, -1.0f));
        temporaryDestinationNode.setLocalRotation(new Quaternion(new Vector3(0f, 0f, 0f)));
    }

    private void buildAnimators(Node temporaryDestinationNode) {
        //compute the start and end position
        Vector3 startWorldPosition = this.getWorldPosition();
        Vector3 finalWorldPosition = temporaryDestinationNode.getWorldPosition();
        //compute the start and end rotation
        Quaternion startWorldRotation = this.getWorldRotation();
        Quaternion finalWorldRotation = temporaryDestinationNode.getWorldRotation();

        buildOrientationAnimator(startWorldRotation, finalWorldRotation);
        buildPositionAnimator(startWorldPosition, finalWorldPosition);
    }

    private void buildPositionAnimator(Vector3 startWorldPosition, Vector3 finalWorldPosition) {
        positionAnimator = new ObjectAnimator();
        positionAnimator.setObjectValues((Object[]) new Vector3[]{startWorldPosition, finalWorldPosition});
        positionAnimator.setEvaluator(new Vector3Evaluator());
        positionAnimator.setDuration(1000);
        positionAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    private void buildOrientationAnimator(Quaternion startWorldRotation, Quaternion finalWorldRotation) {
        orientationAnimator = new ObjectAnimator();
        orientationAnimator.setObjectValues((Object[]) new Quaternion[]{startWorldRotation, finalWorldRotation});
        orientationAnimator.setEvaluator(new QuaternionEvaluator());
        orientationAnimator.setDuration(1000);
        orientationAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    }
}
