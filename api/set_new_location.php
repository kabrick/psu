<?php
include 'dbconfig.php';

$latitude = $_GET['latitude'];
$longitude = $_GET['longitude'];
$altitude = $_GET['altitude'];
$created_by = $_GET['id'];
$pharmacy_id = $_GET['pharmacy'];

//insert values
$sql = "INSERT INTO psu_pharm_cordinates (pharmacy_id, created_by, latitude, longitude, altitude) VALUES ('$pharmacy_id','$created_by','$latitude','$longitude','$altitude')";

$sql2 = "UPDATE psu_pharmacies SET location_set='1', status_image='images/set.png' WHERE id = '$pharmacy_id'";

if ($conn->query($sql) === TRUE) {
	$conn->query($sql2);
    echo "1";
} else {
    echo "0";
}

?>