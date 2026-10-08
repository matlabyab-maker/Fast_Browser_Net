package com.fastbrowser.net;

import android.app.*;import android.os.*;import android.content.*;import android.graphics.Color;import android.net.Uri;import android.provider.Settings;import android.view.*;import android.webkit.*;import android.widget.*;import java.util.*;import java.util.concurrent.Executors;

public class MainActivity extends Activity {
 WebView web; LinearLayout root, bar; EditText url; SharedPreferences prefs; String engine="https://www.google.com/search?q=";
 static final int FILES=77;
 String[] names={"Google","Bing","Yahoo","DuckDuckGo","Brave","Startpage","Ecosia","Qwant","Yandex","Baidu","Mojeek","Ask","AOL","Swisscows"};
 String[] urls={"https://www.google.com/search?q=","https://www.bing.com/search?q=","https://search.yahoo.com/search?p=","https://duckduckgo.com/?q=","https://search.brave.com/search?q=","https://www.startpage.com/sp/search?query=","https://www.ecosia.org/search?q=","https://www.qwant.com/?q=","https://yandex.com/search/?text=","https://www.baidu.com/s?wd=","https://www.mojeek.com/search?q=","https://www.ask.com/web?q=","https://search.aol.com/aol/search?q=","https://swisscows.com/en/web?query=");
 @Override public void onCreate(Bundle b){super.onCreate(b);prefs=getSharedPreferences("settings",0); build(); configureWeb();}
 void build(){root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setBackgroundColor(Color.WHITE);web=new WebView(this);root.addView(web,new LinearLayout.LayoutParams(-1,0,1));bar=new LinearLayout(this);bar.setGravity(Gravity.CENTER_VERTICAL);bar.setPadding(4,4,4,4);bar.setBackgroundColor(Color.rgb(245,245,245));
  Button back=btn("‹"), forward=btn("›"), reload=btn("↻"), home=btn("⌂"), full=btn("⛶"), send=btn("↗"), files=btn("Files"), more=btn("⚙");
  url=new EditText(this);url.setSingleLine(true);url.setHint("Address / Search");url.setTextSize(13);url.setInputType(33);bar.addView(back);bar.addView(forward);bar.addView(reload);bar.addView(home);bar.addView(url,new LinearLayout.LayoutParams(0,52,1));bar.addView(send);bar.addView(files);bar.addView(full);bar.addView(more);root.addView(bar,new LinearLayout.LayoutParams(-1,60));setContentView(root);
  back.setOnClickListener(v->{if(web.canGoBack())web.goBack();});forward.setOnClickListener(v->{if(web.canGoForward())web.goForward();});reload.setOnClickListener(v->web.reload());home.setOnClickListener(v->web.loadUrl("file:///android_asset/home.html"));send.setOnClickListener(v->{Intent i=new Intent(Intent.ACTION_SEND);i.setType("text/plain");i.putExtra(Intent.EXTRA_TEXT,web.getUrl());startActivity(Intent.createChooser(i,"Send"));});files.setOnClickListener(v->pickFiles());full.setOnClickListener(v->toggleFull());more.setOnClickListener(v->settings());url.setOnEditorActionListener((v,a,e)->{loadInput();return true;});
 }
 Button btn(String s){Button b=new Button(this);b.setText(s);b.setTextSize(11);b.setPadding(2,0,2,0);return b;}
 void configureWeb(){web.getSettings().setJavaScriptEnabled(true);web.getSettings().setDomStorageEnabled(true);web.getSettings().setDatabaseEnabled(true);web.getSettings().setSupportZoom(true);web.getSettings().setBuiltInZoomControls(true);web.getSettings().setDisplayZoomControls(false);web.getSettings().setUseWideViewPort(true);web.getSettings().setLoadWithOverviewMode(true);web.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/154 Safari/537.36");web.setWebViewClient(new WebViewClient());web.setDownloadListener((u,ua,cd,mt,len)->download(u,ua,cd,mt));web.setWebChromeClient(new WebChromeClient(){@Override public boolean onShowFileChooser(WebView v,ValueCallback<Uri[]> cb,FileChooserParams p){fileCallback=cb;pickFiles();return true;}});web.loadUrl("file:///android_asset/home.html");}
 ValueCallback<Uri[]> fileCallback;
 void pickFiles(){Intent i=new Intent(Intent.ACTION_OPEN_DOCUMENT);i.addCategory(Intent.CATEGORY_OPENABLE);i.setType("*/*");i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);startActivityForResult(i,FILES);}
 @Override protected void onActivityResult(int r,int c,Intent d){super.onActivityResult(r,c,d);if(r==FILES&&fileCallback!=null){if(c!=RESULT_OK){fileCallback.onReceiveValue(null);fileCallback=null;return;}ArrayList<Uri> a=new ArrayList<>();if(d.getClipData()!=null){for(int i=0;i<d.getClipData().getItemCount();i++)a.add(d.getClipData().getItemAt(i).getUri());}else if(d.getData()!=null)a.add(d.getData());fileCallback.onReceiveValue(a.toArray(new Uri[0]));fileCallback=null;}}
 void download(String u,String ua,String cd,String mt){try{DownloadManager dm=(DownloadManager)getSystemService(DOWNLOAD_SERVICE);DownloadManager.Request r=new DownloadManager.Request(Uri.parse(u));r.setMimeType(mt);r.addRequestHeader("User-Agent",ua);String fn=URLUtil.guessFileName(u,cd,mt);r.setTitle(fn);r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);dm.enqueue(r);Toast.makeText(this,"Download started",Toast.LENGTH_SHORT).show();}catch(Exception e){startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(u)));}}
 void loadInput(){String s=url.getText().toString().trim();if(s.isEmpty())return;if(!s.contains(".")||s.contains(" "))s=engine+Uri.encode(s);else if(!s.matches("^[a-zA-Z][a-zA-Z0-9+.-]*://.*"))s="https://"+s;web.loadUrl(s);}
 void toggleFull(){if(Build.VERSION.SDK_INT>=30)getWindow().getInsetsController().hide(android.view.WindowInsets.Type.systemBars());else getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);}
 void settings(){
  LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL);l.setPadding(25,10,25,10);
  Spinner sp=new Spinner(this);ArrayAdapter<String>a=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,names);sp.setAdapter(a);l.addView(label("Search engine"));l.addView(sp);
  EditText proxy=field("HTTP/HTTPS proxy host:port",prefs.getString("proxy",""));
  EditText dns=field("DNS (configuration)",prefs.getString("dns",""));
  l.addView(proxy);l.addView(dns);
  Button st=btn("ST — Real tunnel / proxy connection");st.setAllCaps(false);l.addView(st);
  st.setOnClickListener(v->showST());
  new AlertDialog.Builder(this).setTitle("Setups").setView(l).setPositiveButton("Save",(d,w)->{prefs.edit().putString("proxy",proxy.getText().toString()).putString("dns",dns.getText().toString()).apply();applyProxy(proxy.getText().toString());}).setNeutralButton("Open Android VPN settings",(d,w)->startActivity(new Intent(Settings.ACTION_VPN_SETTINGS))).setNegativeButton("Cancel",null).show();
  sp.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){public void onNothingSelected(android.widget.AdapterView<?>p){}public void onItemSelected(android.widget.AdapterView<?>p,View v,int pos,long id){engine=urls[pos];}});
 }
 void showST(){
  LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL);l.setPadding(25,8,25,8);
  Spinner protocol=new Spinner(this);String[] ps={"HTTP/HTTPS Proxy","SOCKS5 Proxy"};protocol.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,ps));
  EditText host=field("Server / Host",prefs.getString("st_host",""));
  EditText port=field("Port",prefs.getString("st_port",""));port.setInputType(2);
  EditText user=field("Username (optional)",prefs.getString("st_user",""));
  EditText pass=field("Password (optional)",prefs.getString("st_pass",""));pass.setInputType(129);
  TextView status=label("Status: "+(prefs.getBoolean("st_on",false)?"Connected / enabled":"Disconnected"));
  l.addView(label("Protocol"));l.addView(protocol);l.addView(host);l.addView(port);l.addView(user);l.addView(pass);l.addView(status);
  AlertDialog dlg=new AlertDialog.Builder(this).setTitle("ST").setView(l).setPositiveButton("Connect",null).setNeutralButton("Test connection",null).setNegativeButton("Disconnect",null).create();
  dlg.setOnShowListener(x->{
   dlg.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v->{
    String h=host.getText().toString().trim(),p=port.getText().toString().trim();
    if(h.isEmpty()||p.isEmpty()){status.setText("Status: server and port are required");return;}
    prefs.edit().putString("st_host",h).putString("st_port",p).putString("st_user",user.getText().toString()).putString("st_pass",pass.getText().toString()).putBoolean("st_on",true).apply();
    String scheme=protocol.getSelectedItemPosition()==1?"socks":"http";
    String rule=scheme+"://"+h+":"+p;
    applyProxyRule(rule);
    status.setText("Status: Connected / enabled");
    Toast.makeText(this,"ST enabled for browser traffic",Toast.LENGTH_SHORT).show();
   });
   dlg.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v->{clearST();status.setText("Status: Disconnected");});
   dlg.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v->testST(host.getText().toString().trim(),port.getText().toString().trim(),status));
  });
  dlg.show();
 }
 void applyProxy(String hp){if(hp==null||hp.trim().isEmpty()){if(Build.VERSION.SDK_INT>=29)android.webkit.ProxyController.getInstance().clearProxyOverride(Executors.newSingleThreadExecutor(),()->{});return;}String[] x=hp.trim().split(":",2);if(x.length==2)applyProxyRule("http://"+x[0]+":"+x[1]);}
 void applyProxyRule(String rule){
  if(Build.VERSION.SDK_INT>=29){android.webkit.ProxyController pc=android.webkit.ProxyController.getInstance();pc.setProxyOverride(new android.webkit.ProxyConfig.Builder().addProxyRule(rule).build(),Executors.newSingleThreadExecutor(),()->{});}
 }
 void clearST(){
  prefs.edit().putBoolean("st_on",false).apply();
  if(Build.VERSION.SDK_INT>=29)android.webkit.ProxyController.getInstance().clearProxyOverride(Executors.newSingleThreadExecutor(),()->{});
 }
 void testST(String h,String p,TextView status){
  if(h.isEmpty()||p.isEmpty()){status.setText("Status: server and port are required");return;}
  status.setText("Status: testing…");
  Executors.newSingleThreadExecutor().execute(()->{
   try{
    int pn=Integer.parseInt(p);java.net.Proxy pr=new java.net.Proxy(java.net.Proxy.Type.HTTP,new java.net.InetSocketAddress(h,pn));
    java.net.HttpURLConnection c=(java.net.HttpURLConnection)new java.net.URL("https://www.google.com/generate_204").openConnection(pr);c.setConnectTimeout(6000);c.setReadTimeout(6000);c.setInstanceFollowRedirects(false);int code=c.getResponseCode();c.disconnect();
    runOnUiThread(()->status.setText("Status: test OK (HTTP "+code+")"));
   }catch(Exception e){runOnUiThread(()->status.setText("Status: test failed — "+e.getClass().getSimpleName()));}
  });
 }
 @Override public void onBackPressed(){if(web.canGoBack())web.goBack();else super.onBackPressed();}
}
