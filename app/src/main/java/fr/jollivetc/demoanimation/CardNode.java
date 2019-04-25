package fr.jollivetc.demoanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.util.Log;
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

    private boolean isPresented;
    private AnchorNode anchor;
    private ObjectAnimator positionAnimator;
    private ObjectAnimator orientationAnimator;

    public CardNode(AnchorNode anchor){
        this.anchor = anchor;
        isPresented = false;
        setOnTapListener(this);
        this.setParent(anchor);
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
        if (isPresented){

            Vector3 startWorldPosition = this.getWorldPosition();
            Quaternion startWorldRotation = this.getWorldRotation();
            Vector3 finalWorldPosition = anchor.getWorldPosition();
            Node temporaryfinalNode = new Node();
            temporaryfinalNode.setParent(anchor);
            temporaryfinalNode.setLocalRotation(new Quaternion(new Vector3(-90f,0f,0f)));
            Quaternion finalWorldRotation = temporaryfinalNode.getWorldRotation();

            positionAnimator = new ObjectAnimator();
            positionAnimator.setObjectValues((Object[]) new Vector3[]{startWorldPosition, finalWorldPosition});
            positionAnimator.setEvaluator(new Vector3Evaluator());
            positionAnimator.setDuration(1000);
            positionAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            positionAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    Log.i("DEMO_ANIMATION", "end of animation");
                    CardNode.this.setParent(anchor);
                    CardNode.this.setLocalPosition(new Vector3(0.0f,0.0f,0.0f));
                    CardNode.this.setLocalRotation(new Quaternion(new Vector3(-90f,0f,0f)));
                    positionAnimator = null;
                    orientationAnimator = null;
                }
            });
            orientationAnimator = new ObjectAnimator();
            orientationAnimator.setObjectValues((Object[])new Quaternion[]{startWorldRotation, finalWorldRotation});
            orientationAnimator.setEvaluator(new QuaternionEvaluator());
            orientationAnimator.setDuration(1000);
            orientationAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            positionAnimator.start();
            orientationAnimator.start();
        }else{
            Vector3 startWorldPosition = this.getWorldPosition();
            Quaternion startWorldRotation = this.getWorldRotation();
            Node temporaryDestinationNode = new Node();
            temporaryDestinationNode.setParent(getScene().getCamera());
            temporaryDestinationNode.setLocalPosition(new Vector3(0.0f, 0.0f, -1.0f));
            temporaryDestinationNode.setLocalRotation(new Quaternion(new Vector3(0f,0f,0f)));
            Vector3 finalWorldPosition = temporaryDestinationNode.getWorldPosition();
            Quaternion finalWorldRotation = temporaryDestinationNode.getWorldRotation();
            positionAnimator = new ObjectAnimator();
            positionAnimator.setObjectValues((Object[]) new Vector3[]{startWorldPosition, finalWorldPosition});
            positionAnimator.setEvaluator(new Vector3Evaluator());
            positionAnimator.setDuration(1000);
            positionAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            positionAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    Log.i("DEMO_ANIMATION", "end of animation");
                    CardNode.this.setParent(getScene().getCamera());
                    CardNode.this.setLocalPosition(new Vector3(0.0f, 0.0f, -1.0f));
                    CardNode.this.setLocalRotation(new Quaternion(new Vector3(0f,0f,0f)));
                    positionAnimator = null;
                    orientationAnimator = null;
                }
            });
            orientationAnimator = new ObjectAnimator();
            orientationAnimator.setObjectValues((Object[])new Quaternion[]{startWorldRotation, finalWorldRotation});
            orientationAnimator.setEvaluator(new QuaternionEvaluator());
            orientationAnimator.setDuration(1000);
            orientationAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            positionAnimator.start();
            orientationAnimator.start();
        }
        this.isPresented = !this.isPresented;




    }
}
