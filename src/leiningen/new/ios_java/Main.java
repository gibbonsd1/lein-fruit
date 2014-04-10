package {{package}};

import org.robovm.apple.coregraphics.*;
import org.robovm.apple.foundation.*;
import org.robovm.apple.uikit.*;

public class {{class-name}} extends UIApplicationDelegateAdapter {
	private UIWindow window = null;
	private int clickCount = 0;
	
	public boolean didFinishLaunching(UIApplication app, NSDictionary opts) {
		final UIButton button = UIButton.create(UIButtonType.RoundedRect);
		button.setFrame(new CGRect(115.0f, 121.0f, 91.0f, 37.0f));
		button.setTitle("Click me!", UIControlState.Normal);
		
		button.addOnTouchUpInsideListener(new UIControl.OnTouchUpInsideListener() {
			public void onTouchUpInside(UIControl control, UIEvent event) {
				button.setTitle("Click #" + (++clickCount), UIControlState.Normal);
			}
		});
		
		window = new UIWindow(UIScreen.getMainScreen().getBounds());
		window.setBackgroundColor(UIColor.colorLightGray());
		window.addSubview(button);
		window.makeKeyAndVisible();
		
		return true;
	}

	public static void main(String[] args) {
		NSAutoreleasePool pool = new NSAutoreleasePool();
		UIApplication.main(args, null, {{class-name}}.class);
		pool.close();
	}
}
