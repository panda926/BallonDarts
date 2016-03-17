/**
 * 
 */
package game.com;

import org.cocos2d.transitions.*;

import android.media.MediaPlayer;

/**
 * @author Administrator
 *
 */
public class Global {
	public static float 			rWidth = 0.0f;
	public static float 			rHeight = 0.0f;
	public static float 			rScale_X = 0.0f;
	public static float 			rScale_Y = 0.0f;
	public static float 			rsScale_X = 0.0f;
	public static float 			rsScale_Y = 0.0f;
	
	public static int 				g_nTotalScore = 0;
	public static int 				g_nUserNum = 0;
	public static int 				g_nPUserNum = 0;
	public static boolean			g_fProgressivePlay = false;
	public static boolean			g_fGameDone = false;
	
	static Class<?> transitions[] = {
        JumpZoomTransition.class,
        FadeTransition.class,
        ShrinkGrowTransition.class,
        RotoZoomTransition.class,
        MoveInLTransition.class,
        MoveInRTransition.class,
        MoveInTTransition.class,
        MoveInBTransition.class,
        SlideInLTransition.class,
        SlideInRTransition.class,
        SlideInTTransition.class,
        SlideInBTransition.class,
	};
	
	public static   String[] mpUserName = new String[GameConfig.MAX_USER_NUM];
	public static   int[]    mpScore = new int[GameConfig.MAX_USER_NUM];
	public static   String[] mUserName = new String[GameConfig.MAX_USER_NUM];
	public static   int[]    mScore = new int[GameConfig.MAX_USER_NUM];
	
	// Sound Manager
	public static MediaPlayer m_mainSound, m_throwSound, m_readySound, m_goSound, m_buttonSound,
							m_dartHitSound, m_canvasSound, m_scoreSound, m_bonusSound;
}
