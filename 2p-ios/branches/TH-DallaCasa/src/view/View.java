package view;
import org.robovm.apple.foundation.*;
import org.robovm.apple.uikit.*;

import controller.ViewController;

public class View extends UIApplicationDelegateAdapter {

    private UIWindow window = null;
    private ViewController viewController = null;
    
    @SuppressWarnings("rawtypes")
    @Override
    public boolean didFinishLaunching(UIApplication application,
            NSDictionary launchOptions) {

        window = new UIWindow(UIScreen.getMainScreen().getBounds());
        viewController = new ViewController(this);
        window.setRootViewController(viewController);
        
        
        window.makeKeyAndVisible();
        
        return true;
    }
    
    public void addSubview(UIView view) {
    	window.addSubview(view);
    }

    public static void main(String[] args) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(args, null, View.class);
        pool.close();
    }
}