# IoT Research Project
Touch-based prediction model for user behavior with mobile sensor data

## Project breakdown
- Android mobile application for sensor data collection : accelerometer, gyroscope, magnetometer,proximity
- Sensor data updated based on sensor reading changed (high frequency update) and can be uploaded to firebase with the same frequency
- The sensor data is then exported and cleansed into cvs file in the dataSouce folder
- The data training code was running in google collab and tested svm and random forest classifier
- The concept of digital twins were implemented during the data training and testing process as it acts as a learning feedback feeding a boolean value back to the training model

## Results
<img width="531" alt="Screenshot 2023-11-30 at 11 45 04 AM" src="https://github.com/AlenkaTang1/IoT/assets/112491096/02c4bd73-2c30-41d7-b210-1c261816707b">
<img width="531" alt="Screenshot 2023-11-30 at 11 45 52 AM" src="https://github.com/AlenkaTang1/IoT/assets/112491096/d32c2711-9415-4172-97d2-4fcb8a12b293">

## svm confusion matrix with and without digial twin

<img width="300" alt="Screenshot 2023-11-30 at 11 47 49 AM" src="https://github.com/AlenkaTang1/IoT/assets/112491096/f6388a32-0fd2-43d7-9008-4f3be2f08425">
vs
<img width="281" alt="Screenshot 2023-11-30 at 11 48 57 AM" src="https://github.com/AlenkaTang1/IoT/assets/112491096/97362d8f-6855-466e-b6ca-7261c36d8a23">

## 
<img width="274" alt="Screenshot 2023-11-30 at 11 49 43 AM" src="https://github.com/AlenkaTang1/IoT/assets/112491096/a6cde610-5179-4d90-aec9-dd35601adf66">


