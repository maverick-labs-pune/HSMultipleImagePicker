#Synopsis

HSMultipleImagePicker is a library for Android which allows you to pick multiple images from the phone. It follows the Material Design specs and will fit right into your new Material app. The design has been inspired by the WhatsApp Image Picker.

- It will follow your app theme for colors.
- You can add a limit for the max number of photos that can be added by user.
- If your theme is NoActionBar, this library can add the toolbar for you. Just set the ```showSeparateToolbar``` option to true

HSMultipleImagePicker was designed for HindSites app. 

HindSites :
https://play.google.com/store/apps/details?id=com.hindsitesapp.hindsites

##Code Example

Add this to your root build.gradle

```gradle
    allprojects {
        repositories {
            jcenter()
            maven { url "https://jitpack.io" }
        }
   }
   dependencies {
        compile 'com.github.vishakhadamle:HSMultipleImagePicker:1.0.1'
   }
```

Add this code where you want to add the image picker -

```java
private void selectPhotos() {

        //For supporting api > 23, need to check permission to read external storage.
        if(checkPermission() == PackageManager.PERMISSION_GRANTED) {
            int maxPhotos = 10;
            multiImagePicker = new MultiImagePicker.Builder()

                    //Maximum number of photos that user is allowed to select. By default, there is no limit
                    .setMaxPhotos(maxPhotos)

                    // If your theme is NoActionBar - as in this sample app or you are hiding the bar in your activity,
                    // set this option to true in order to show a toolbar at the top.
                    // Toolbar will follow your app theme.
                    // Default is false.
                    .showSeparateToolbar(true)

                    //Receiver to get all the selected photos
                    .setOnReceiveListener(new MultiImagePickerListener() {
                        @Override
                        public void onImagesPicked(List<PickedPhoto> pickedPhotos) {
                            //Get all the picked photos here . Type of photo is PickedPhoto
                            for (PickedPhoto photo : pickedPhotos) {
                                Log.d("TAG", " In main activity " + photo.getPhotoPath());

                                //You can also get these values from the picked photos.
                                //photo.getLatitude();
                                //photo.getLongitude();
                                //photo.getClickedDateTime();
                            }
                        }
                    })
                    .build();
            multiImagePicker.startActivity(getApplicationContext());
        } else {
            requestPermission();
        }
    }
```


## Motivation

Android does not support Multiple image picker for API < 18. For newer APIs, you can tell the default picker provided by Google to allow Multiple Images to be picked. However, we received a lot of complains from HindSites users initially when we were using EXTRA_ALLOW_MULTIPLE option with the default Google Image Picker.

That is because, if the user has some other Gallery app selected as the default Image Picker, that Gallery App is not guaranteed to support Multiple Image picking. As a result, most of our users got frustrated that they could not select multiple photos. 

We decided to take matters into our own hands and designed the HSMultipleImagePicker.


## Contribution

### Questions

If you have any questions regarding the HSMultipleImagePicker, create an Issue or mail support@hindsitesapp.com.

### Feature request

To create a new Feature request, open an issue 
