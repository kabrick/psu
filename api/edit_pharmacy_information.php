<?php
include 'dbconfig.php';

$id = $_GET['id'];
$pharmacy = $_GET['name'];
$location = $_GET['location'];

//update values
$sql = "UPDATE psu_pharmacies SET pharmacy='$pharmacy', location='$location' WHERE id = '$id'";

if ($conn->query($sql) === TRUE) {
    echo "1";
} else {
    echo "0";
}

?>