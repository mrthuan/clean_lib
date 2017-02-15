# clean_lib
gradle project :    maven { url 'https://jitpack.io' }
gradle app : compile 'com.github.mrthuan:clean_lib:1.0' 
</br>
add lib notication box
key enable : Prefs.with(getApplicationContext()).writeBoolean("enable_notibox", true);
xlm :  
      <io.mon.noticationbox.NotificationBox
       android:id="@+id/notificationBox"
       app:ShowBackPress="false"
       android:layout_width="match_parent"
       android:layout_height="wrap_content" />
       
java :
  NotificationBox notificationBox = (NotificationBox) findViewById(R.id.notificationBox);
  notificationBox.Build();

notificationBox.OnbackPress(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //code here
           }
       });
       
