<?php
include 'dbconfig.php';

$latitude = $_GET['latitude'];
$longitude = $_GET['longitude'];
$time = $_GET['time'];
$id = $_GET['id'];

$sql_id = "SELECT id FROM psu_pharmacies WHERE update_by = '$id' LIMIT 1";

$result_id = $conn->query($sql_id);
$result_assoc_id = $result_id->fetch_assoc();
$pharmacy_id = $result_assoc_id['id'];

$date = date('Y-m-d');

$sql = "INSERT INTO psu_attendance_log (pharmacy_id, psu_id, latitude_in, longitude_in, time_in, date) VALUES ('$pharmacy_id','$id','$latitude','$longitude','$time','$date')";

if ($conn->query($sql) === TRUE) {
    echo $conn->insert_id;
} else {
    echo "0";
}

?>