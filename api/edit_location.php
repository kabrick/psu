<?php
include 'dbconfig.php';

$latitude = $_GET['latitude'];
$longitude = $_GET['longitude'];
$altitude = $_GET['altitude'];
$created_by = $_GET['id'];
$pharmacy_id = $_GET['pharmacy'];

//update values
$sql = "UPDATE psu_pharm_cordinates SET created_by='$created_by',latitude='$latitude',longitude='$longitude',altitude='$altitude' WHERE pharmacy_id = '$pharmacy_id'";

if ($conn->query($sql) === TRUE) {
    echo "1";
} else {
    echo "0";
}

?>