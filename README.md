Name: William Lee
Computing ID: sgw5ne

# Homework 1
Homework 1 should be located in the folder labeled "BucketList" instead of "bucketlistsgw5ne" due to some file creation errors that occurred making it easier to just start over, but for now the 
old folder is still there just for the sake of commit history.

Homework 1 AI Usage: AI was primarily used to help with implementation of Date Picker, but also to help implementing Activities and certain screen formatting. All AI generated code was again hand reviewed as it was written
into the codebase, with testing by running code and ensuring all necessary functions were being fulfilled.

# Homework 2
Homework 2 should be located in the folder labeled "collegefootballscores".

Homework 2 AI Usage: AI was primarily used to help with the implementation of the database and to help with some syntax issues that came up while I was building this app. All AI generated code was again hand reviewed as it was written in, with testing done by running code and ensuring all necessary functions were being fulfilled.

# Homework 3

## Sampling Rate
I went with SENSOR_DELAY_GAME (about 50 readings a second) for the accelerometer because it's the slowest
setting that still catches each step clearly. A step happens roughly every half second, so at 50 Hz there
are enough readings per step to smooth out the noise without flattening the peak. SENSOR_DELAY_UI felt too
slow to reliably catch peaks, and FASTEST would just burn battery for such minimal gain. The hardware step counter
runs on a low-power chip, so it gets the slowest setting, and both listeners only run while the app is open.
For the threshold, a phone sitting still reads about 9.8 m/s^2 from gravity, so I started a bit above that at
11.0 and required steps to be at least 0.3 seconds apart. My unit tests showed it counts nothing when the
phone is still and counts normal walking correctly, though really gentle steps can get missed.


## Ethics Reflection
Daily step counts seem harmless, but after a few weeks they could hint at things like illness, depression,
a new schedule, or when someone usually isn't home. To keep things minimal, the app only saves one number per
day, throws away the raw sensor data right after using it, and never sends anything off the phone since it
doesn't have internet access. If someone says no to the permission, I don't sneak around it by counting steps
from the accelerometer anyway. Right now the history sticks around until the app is deleted, so I'd want to add
auto-deletion after a few months and a button to clear everything. For consent, the app only asks when you
actually go to use step counting, explains why in one sentence, and still works fine if you say no.

Homework 3 AI Usage: AI was primarily used with implementing the database and helping with debugging code as well as figuring out the workflow necessary for the app. All AI generated code was hand reviewed as it was written in, with written test cases made for this homework to ensure functionality.
