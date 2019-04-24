package fr.jollivetc.demoanimation;

import android.util.Log;
import android.view.MotionEvent;

import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;

public class CardNode extends Node implements Node.OnTapListener {

    private boolean isPresented;
    private AnchorNode anchor;

    public CardNode(AnchorNode anchor){
        this.anchor = anchor;
        isPresented = false;
        setOnTapListener(this);
        this.setParent(anchor);
    }

    @Override
    public void onTap(HitTestResult hitTestResult, MotionEvent motionEvent) {
        if (isPresented){
            this.setParent(anchor);
            this.setLocalPosition(new Vector3(0.0f,0.0f,0.0f));
            this.setLocalRotation(new Quaternion(new Vector3(-90f,0f,0f)));
        }else{

            this.setParent(getScene().getCamera());
            this.setLocalPosition(new Vector3(0.0f, 0.0f, -1.0f));
            this.setLocalRotation(new Quaternion(new Vector3(0f,0f,0f)));
        }
        this.isPresented = !this.isPresented;
    }
}
