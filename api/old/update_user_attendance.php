<?php
include 'dbconfig.php';

$id = $_GET['id'];
$time = $_GET['time'];
$day = $_GET['day'];
$latitude = $_GET['latitude'];
$longitude = $_GET['longitude'];

$sql = "UPDATE psu_attendance_log SET time_out='$time',day_id='$day',latitude_out='$latitude',longitude_out='$longitude',working_hours='1' WHERE id = '$id'";

if ($conn->query($sql) === TRUE) {
    echo "1";
} else {
    echo "0";
}

?>