package game.com;

import java.lang.reflect.Constructor;
import org.cocos2d.actions.ActionManager;
import org.cocos2d.layers.Layer;
import org.cocos2d.menus.Menu;
import org.cocos2d.menus.MenuItem;
import org.cocos2d.menus.MenuItemImage;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.nodes.TextureManager;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.transitions.TransitionScene;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class BalloonDarts extends Activity {
	public CCGLSurfaceView mGLSurfaceView;	

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mGLSurfaceView = new CCGLSurfaceView(this);
        setContentView(mGLSurfaceView);
    }
    @Override
    public void onStart() {
        super.onStart();
        getScaledCoordinate();
        loadMusic();
        // attach the OpenGL view to a window
        Director.sharedDirector().attachInView(mGLSurfaceView);     
        Scene scene = Scene.node();
        scene.addChild(new StartView(), 1);
        //Make the Scene active
        Director.sharedDirector().runWithScene(scene);
    }

    @Override
    public void onPause() {
        super.onPause();
        freeMediaPlayer();
        ActionManager.sharedManager().removeAllActions();
	    TextureManager.sharedTextureManager().removeAllTextures();
//        Director.sharedDirector().pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Director.sharedDirector().resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        freeMediaPlayer();
        ActionManager.sharedManager().removeAllActions();
	    TextureManager.sharedTextureManager().removeAllTextures();
    }
    
    public void freeMediaPlayer()
    {
    	if(Global.m_mainSound != null)
    	{
    		Global.m_mainSound.pause();
    		Global.m_mainSound.release();
    		Global.m_mainSound = null;
    	}
    	
    	if(Global.m_throwSound != null)
    	{
    		Global.m_throwSound.pause();
    		Global.m_throwSound.release();
    		Global.m_throwSound = null;
    	}
    	
    	if(Global.m_readySound != null)
    	{
    		Global.m_readySound.pause();
    		Global.m_readySound.release();
    		Global.m_readySound = null;
    	}
    	
    	if(Global.m_goSound != null)
    	{
    		Global.m_goSound.pause();
    		Global.m_goSound.release();
    		Global.m_goSound  = null;
    	}
    	
    	if(Global.m_buttonSound != null)
    	{
    		Global.m_buttonSound.pause();
    		Global.m_buttonSound.release();
    		Global.m_buttonSound = null;
    	}
    	
    	if(Global.m_dartHitSound != null)
    	{
    		Global.m_dartHitSound.pause();
    		Global.m_dartHitSound.release();
    		Global.m_dartHitSound = null;
    	}
    	
    	if(Global.m_canvasSound != null)
    	{
    		Global.m_canvasSound.pause();
    		Global.m_canvasSound.release();
    		Global.m_canvasSound = null;
    	}
    	
    	if(Global.m_scoreSound != null)
    	{
    		Global.m_scoreSound.pause();
    		Global.m_scoreSound.release();
    		Global.m_scoreSound = null;
    	}
    	
    	if(Global.m_bonusSound != null)
    	{
    		Global.m_bonusSound.pause();
    		Global.m_bonusSound.release();
    		Global.m_bonusSound = null;
    	}
    }
    
    private void getScaledCoordinate()
	{
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		
		Global.rWidth = displayMetrics.widthPixels;
		Global.rHeight = displayMetrics.heightPixels;
		Global.rScale_X = Global.rWidth / 480.0f;
		Global.rScale_Y = Global.rHeight / 320.0f;
		Global.rsScale_X = Global.rWidth / 1024.0f;
		Global.rsScale_Y = Global.rHeight/768.0f;
	}
    
    private void loadMusic()
    {
    	Global.m_mainSound = MediaPlayer.create(Director.sharedDirector().getActivity().getApplicationContext(), R.raw.balloon_music);
    	Global.m_throwSound = MediaPlayer.create(Director.sharedDirector().getActivity().getApplicationContext(), R.raw.throw_);
    	Global.m_readySound = MediaPlayer.create(Director.sharedDirector().getActivity().getApplicationContext(), R.raw.ready);
    	Global.m_goSound = MediaPlayer.create(Director.sharedDirector().getActivity().getApplicationContext(), R.raw.go);
    	Global.m_buttonSound = MediaPlayer.create(Director.sharedDirector().getActivity().getApplicationContext(), R.raw.button);
    	Global.m_dartHitSound = MediaPlayer.create(Director.sharedDirector().getActivity().getApplicationContext(), R.raw.dart_hit_wood);
    	Global.m_canvasSound = MediaPlayer.create(Director.sharedDirector().getActivity().getApplicationContext(), R.raw.canvas);
    	Global.m_scoreSound = MediaPlayer.create(Director.sharedDirector().getActivity().getApplicationContext(), R.raw.bell);
    	Global.m_bonusSound = MediaPlayer.create(Director.sharedDirector().getActivity().getApplicationContext(), R.raw.bonus_time);
    	
    }
    static TransitionScene newScene(float d, int mode, Scene s)
    {
    	 try {
             Class<?> c = Global.transitions[mode];
             Class<?> partypes[] = new Class[2];
             partypes[0] = Float.TYPE;
             partypes[1] = s.getClass();
             Constructor<?> ctor = c.getConstructor(partypes);
             Object arglist[] = new Object[2];
             arglist[0] = d;
             arglist[1] = s;
             return (TransitionScene) ctor.newInstance(arglist);
         } catch (Exception e) {
             return null;
         }
    }
    
    static class StartView extends Layer
    {
    	int 	m_nTick;
    	int     m_nWheelNum;
    	static Sprite loadingSprite;
    	static Sprite m_balloonSprite;
//    	static Sprite m_wheelSprite;
    	Sprite m_wheelSprite[];
    	static String strTemp;
    	
    	public StartView()
    	{
    		m_nTick = 0;
    		m_nWheelNum = 1;
    		//Global.init();
    		loadingSprite = Sprite.sprite("gfx/load.gif");
    		loadingSprite.setPosition(Global.rWidth / 2, Global.rHeight / 2);
    		loadingSprite.setScaleX(Global.rScale_X); 
    		loadingSprite.setScaleY(Global.rScale_Y);
     		addChild(loadingSprite, 1);
     		m_wheelSprite = new Sprite[4];
    		for(int i = 0; i < 4; i ++)
     		{
     			String str = String.format("gfx/ferriswheel%d.gif", i + 1);
     			m_wheelSprite[i] = Sprite.sprite(str);
     			m_wheelSprite[i].setScaleX(Global.rScale_X); m_wheelSprite[i].setScaleY(Global.rScale_Y);
     			m_wheelSprite[i].setPosition(Global.rWidth * 4 / 5, Global.rHeight * 3 / 5);
     			addChild(m_wheelSprite[i], 2);
     			m_wheelSprite[i].setVisible(false);
     		}
     		schedule("timerEvent", 0.2f);
    	 }
    	
	 	public void timerEvent(float dt)
    	{
    		m_nTick ++;
	       	if(m_nTick > GameConfig.LOADING_DELAY_TIME)
	       	{
	       		removeChild(loadingSprite, true);
	       		loadingSprite = null;
	       		loadButtons();
	       		unschedule("timerEvent");
	       		m_nTick = 0;
	       	}
    	}
    	 	
    	 public void play()
    	 {
    		 this.unschedule("turnWheel");
    		 if(strTemp != null)
    		 {
    			 strTemp = null;
    		 }
    		 
    		 if(Global.m_buttonSound != null)
    			 Global.m_buttonSound.start();
    		 System.gc();
 			
    		 Scene scene = Scene.node();
    		 scene.addChild(new MainMenu(), 1);
	         Director.sharedDirector().replaceScene(newScene(GameConfig.TRANSITION_DURATION, 1, scene));
	         
	     }
    	 public void turnWheel(float dt)
    	 {
    		 float rTemp_X  = m_balloonSprite.getPositionX();
    		 float rTemp_Y = m_balloonSprite.getPositionY();
    		 if(rTemp_X < -20.0f * Global.rScale_X || rTemp_Y > 440.0f * Global.rScale_Y)
    		 {
    			 m_balloonSprite.setPosition(Global.rWidth / 3, Global.rHeight / 2);
    		 }else
    		 {
    			m_balloonSprite.setPosition(rTemp_X - GameConfig.SAMPLE_BALLOON_SPEED_X * Global.rScale_X,
    				 						rTemp_Y + GameConfig.SAMPLE_BALLOON_SPEED_Y * Global.rScale_Y); 
    		 }
    		
    		 m_nWheelNum ++;
    		 if(m_nWheelNum == GameConfig.WHEEL_ANI_NUM)
    			 m_nWheelNum = 0;
    		 
    		 for(int i = 0; i < 4; i ++)
    			 m_wheelSprite[i].setVisible(false);
    		 m_wheelSprite[m_nWheelNum].setVisible(true);
    		 
    	 }
    	 private void loadButtons()
    	 {
    		Sprite  bgSprite = Sprite.sprite("gfx/bg_title_bkg.gif");
			bgSprite.setPosition(Global.rWidth / 2, Global.rHeight / 2);
			bgSprite.setScaleX(Global.rScale_X);
			bgSprite.setScaleY(Global.rScale_Y);
			addChild(bgSprite, 1);
			
			Sprite bgSprite3 = Sprite.sprite("gfx/coaster_bg.gif");
			bgSprite3.setPosition(Global.rWidth / 2, Global.rHeight / 2);
			bgSprite3.setScaleX(Global.rScale_X);
			bgSprite3.setScaleY(Global.rScale_Y);
			addChild(bgSprite3, 1);
			
			Sprite bgSprite4 = Sprite.sprite("gfx/ferris_support.gif");
			bgSprite4.setPosition(Global.rWidth* 4 / 5, Global.rHeight * 2 / 5);
			bgSprite4.setScaleX(Global.rScale_X);
			bgSprite4.setScaleY(Global.rScale_Y);
			addChild(bgSprite4, 1);
			
			m_balloonSprite = Sprite.sprite("gfx/lostballoon.gif");
			m_balloonSprite.setPosition(Global.rWidth / 3, Global.rHeight / 2);
			m_balloonSprite.setScaleX(Global.rScale_X);
			m_balloonSprite.setScaleY(Global.rScale_Y);
			addChild(m_balloonSprite, 1);

			Sprite  bgSprite1 = Sprite.sprite("gfx/bg_title_booths_balloon.gif");
			bgSprite1.setPosition(Global.rWidth / 2, Global.rHeight / 5);
			bgSprite1.setScaleX(Global.rScale_X);
			bgSprite1.setScaleY(Global.rScale_Y);
			addChild(bgSprite1, 1);		
			
			Sprite  bgSprite2 = Sprite.sprite("gfx/boardwalklogo_balloon.gif");
			bgSprite2.setPosition(Global.rWidth / 2, Global.rHeight * 3 / 5);
			bgSprite2.setScaleX(Global.rScale_X);
			bgSprite2.setScaleY(Global.rScale_Y);
			addChild(bgSprite2, 1);
				
     		MenuItem btnEnter = MenuItemImage.item("gfx/enter_btn.gif" ,
     												"gfx/enter_btn_over.gif",
     												this,
     												"play");
     		btnEnter.setScaleX(Global.rScale_X);
     		btnEnter.setScaleY(Global.rScale_Y);
     		Menu menu = Menu.menu(btnEnter);
 	        menu.setPosition(0, 0);
     		btnEnter.setPosition(Global.rWidth/2, Global.rHeight / 6);
     		addChild(menu, 1);
     		
     		this.schedule("turnWheel", 0.1f);
     		//soundPlay();
     		if(Global.m_mainSound != null)
     		{
				Global.m_mainSound.start();
				Global.m_mainSound.setLooping(true);
     		}     		
    	 }    	 
    }    
}