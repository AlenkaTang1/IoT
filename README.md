# IoT Research Project
Touch-based prediction model for user behavior with mobile sensor data

## Project breakdown
- Android mobile application for sensor data collection : accelerometer, gyroscope, magnetometer,proximity
- Sensor data updated based on sensor reading changed (high frequency update) and can be uploaded to firebase with the same frequency
- The sensor data is then exported and cleansed into cvs file in the dataSouce folder
- The data training code was running in google collab and tested svm and random forest classifier
- The concept of digital twins were implemented during the data training and testing process as it acts as a learning feedback feeding a boolean value back to the training model


