This is the developer version of Quackstagram and only verified Quacks working at Cheapo Software Solution may access this. If you are not a verified Quack, turn around.

src/
The main method is in SignInUI.java, from which the application can be run. This is not the proper way to do it, but it works!  I heard someone say something about test cases as well, but we are moving to fast for those. Don't let anyone tell you otherwise and introduce them.

src/UI
This contains all the code for the UI that the user sees.

src/Model
This contains all our business logic that is behind the pretty interface.

/data
Passwords and data from the users is stored in plaintext data/credentials.txt for easy access if you ever need to change something.
Other relevant data from the application are stored in .txt files under /data as well (for now).

/img All the images are stored here. Used-uploaded images are stored under /img/uploaded, with some default images already there. Profile pictures are uploaded under /img/storage/profile. The remaining two folders should not change unless we want to overhaul the visual design again (Don't let them do it! It is beautiful as it is and our users love it!)

image_details.txt contains additional data from all the images our users upload.