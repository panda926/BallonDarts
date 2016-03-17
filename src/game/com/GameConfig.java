/**
 * 
 */
package game.com;

/**
 * @author Administrator
 *
 */
public class GameConfig {
	
	public static int			 WHEEL_ANI_NUM = 4;
	public static int 			 DART_ANI_NUM = 3;
	public static int 			 BALLOON_ANI_NUM = 3;
	public static int			 BALLOON_NUM = 24;

	public static int			 MAX_DELTA_X = 11;
	public static int			 MAX_DELTA_Y = 12;
	public static int			 G = 4800;
	public static float			 DECELERATION_X = 0.02f;

	public static float			 DART_INTERVAL = 0.05f;
	public static float			 DART_SCALE_RATE = 0.94f;
	public static float			 DART_REAL_WIDTH = 65f;//non_needed
	public static float			 DART_REAL_HEIGHT = 83;
	public static float			 BALLOON_REAL_WIDTH = 30;//non_needed
	public static float			 BALLOON_REAL_HEIGHT = 35;

	public static float			 WAND_LEFT_X = 82;
	public static float			 WAND_RIGHT_X = 454;
	public static float			 WAND_TOP_Y = 280;
	public static float			 WAND_BOTTOM_Y = 80;
	public static float			 LCD_POSITION_Y = 295;
	public static int			 FONT_SIZE = 20;
	
	public static int			 MAX_USER_NUM	= 7;
	public static final int      LOADING_DELAY_TIME = 20;
	public static final float	 TRANSITION_DURATION = 1.0f;
	public static final float    SAMPLE_BALLOON_SPEED_X = 3.0f;
	public static final float	 SAMPLE_BALLOON_SPEED_Y = 2.5f;

	public enum SoundType 
	{
		NONE_SOUND,	
		THROW_SOUND,
		READY_SOUND,
		GO_SOUND,
		BUTTON_SOUND,
		DARTHIT_SOUND,
		CANVAS_SOUND,
		SCORE_SOUND,
		BONUS_SOUND,	
	};

	public static int			 MAX_NAME_LENGTH = 20;
	
}
