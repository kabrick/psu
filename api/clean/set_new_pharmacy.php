<?php
include 'dbconfig.php';

$name = $_GET['name'];
$location = $_GET['location'];
$id = $_GET['id'];

//insert values
$sql = "INSERT INTO psu_pharmacies (pharmacy, location, update_by) VALUES ('$name','$location','$id')";

if ($conn->query($sql) === TRUE) {
    echo "1";
} else {
    echo "0";
}

?>