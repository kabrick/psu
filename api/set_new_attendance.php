<?php
include 'dbconfig.php';

// get sent data
$psu_id = $_GET['psu_id'];
$time_in = $_GET['time_in'];
$time_out = $_GET['time_out'];
$latitude_in = $_GET['latitude_in'];
$longitude_in = $_GET['longitude_in'];
$latitude_out = $_GET['latitude_out'];
$longitude_out = $_GET['longitude_out'];
$working_hours = $_GET['working_hours'];
$pharmacy_id = $_GET['pharmacy_id'];
$day_id = $_GET['day_id'];

//insert values
$sql = "INSERT INTO psu_attendance_log (psu_id, time_in, time_out, latitude_in, longitude_in, latitude_out, longitude_out, working_hours, pharmacy_id, day_id) VALUES ('$psu_id','$time_in','$time_out','$latitude_in','$longitude_in','$latitude_out','$longitude_out','$working_hours','$pharmacy_id','$day_id')";

if ($conn->query($sql) === TRUE) {
    echo "1";
} else {
    echo "0";
}

?>