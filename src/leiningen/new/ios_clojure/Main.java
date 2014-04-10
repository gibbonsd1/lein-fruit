package {{package}};

import clojure.lang.RT;
import clojure.lang.Symbol;

import org.robovm.apple.foundation.*;
import org.robovm.apple.uikit.*;

public class {{class-name}} extends UIApplicationDelegateAdapter {
	public boolean didFinishLaunching(UIApplication app, NSDictionary opts) {
		RT.var("clojure.core", "require").invoke(Symbol.intern("{{namespace}}"));
		RT.var("{{namespace}}", "init").invoke();
		return true;
	}

	public static void main(String[] args) {
		NSAutoreleasePool pool = new NSAutoreleasePool();
		UIApplication.main(args, null, {{class-name}}.class);
		pool.close();
	}
}
