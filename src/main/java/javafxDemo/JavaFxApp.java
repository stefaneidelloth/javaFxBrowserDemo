package javafxDemo;

import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class JavaFxApp extends Application {

	private WebView webView;

	private WebEngine engine;

	public static void start() {

		var args = new String[1];
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		webView = new WebView();
		primaryStage.setScene(new Scene(this.webView, 1024, 768));
		primaryStage.show();

		engine = webView.getEngine();

		engine.setJavaScriptEnabled(true);
		
		engine.setOnAlert((eventArgs) -> {
			String message = eventArgs.getData();
			System.out.println("PlainJavaFxD3Browser-Alert: " + message);

		});
		
		engine.setOnError(evt -> {
		    var throwable = evt.getException();
		    if (throwable != null) {
		    	throwable.printStackTrace();
		    }
		});
		
		engine.executeScript("alert('hello from javascript to console')");
		
		createSomeConent();				

		//loadFirebugLite();
		
		loadContentFromExternalFile("./main.js");
		
		checkIfWebComponentsAreSupported();
		
		checkIfEs6ModulesAreSupported();
		
		showVersionInfo();

		// delete cookies
		java.net.CookieHandler.setDefault(new java.net.CookieManager());

	}
	
	private void checkIfWebComponentsAreSupported() {
		var script = "let componentsAreSupported = window.customElements !== undefined;" +
	                 "if(componentsAreSupported){" +
				     "    alert('Custom web components are supported.');" +
	                 "} else {" +
				     "    alert('Cusstom web components are not supported');" +
	                 "}";
		engine.executeScript(script);
		
	}

	private void checkIfEs6ModulesAreSupported() {
		var script = "let es5ModulesAreSupported = 'noModule' in HTMLScriptElement.prototype;" +
                "if(es5ModulesAreSupported){" +
			     "    alert('ES6 modules are supported.');" +
                "} else {" +
			     "    alert('Es6 modules are not supported');" +
                "}";
		engine.executeScript(script);
		
	}
	
	private void showVersionInfo() {
		engine.executeScript("alert('------  Browser information ----');");	
		
		var script = "for(var key in navigator) {"+
	                 "    alert(key + ': ' + navigator[key]);" +
				     "}";		
		engine.executeScript(script);
		
		engine.executeScript("alert('---------------------------------');");	
		
		
	}	
	

	private void createSomeConent() {		

		var initialBrowserContent = //
				"<html>" + //						
						"    <body>" + //
						"        <span>Hello World!</span>" + //
						"    </body>" + //
						"</html>";		

		engine.loadContent(initialBrowserContent);

	}

	private void loadContentFromExternalFile(String filePath) {

		String url = JavaFxApp.class.getResource(filePath).toExternalForm();
		
		var script = "script = document.createElement('script');" +
		             "script.url = '"+ url + "';" +
				"document.body.appendChild(script);";			

		engine.executeScript(script);

	}
	
	/*
	
	private void loadFirebugLite() {
	
		//inclusion with <script src="http://getfirebug.com/releases/lite/1.2/firebug-lite-compressed.js#startOpened"></script>
		//stopped working
		//Therefore, I tried to inlude it offline from file. Did not work, seems to need additional files, e.g. skin
		//and a server that serves those files. 
		//Also see https://stackoverflow.com/questions/58521474/how-to-include-firebug-light-without-access-to-http-getfirebug-com

		var filePath = "./firebug-lite-debug.js";
		var stream = JavaFxApp.class.getResourceAsStream(filePath);
		
		try(var scanner = new Scanner(stream, "UTF-8")){
			var content = scanner.useDelimiter("\\A").next();
			engine.executeScript(content);
		}	

	}
	
	*/

	
	

}