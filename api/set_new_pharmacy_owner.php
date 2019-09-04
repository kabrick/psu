<?php
include 'dbconfig.php';

$name = $_GET['name'];
$location = $_GET['location'];
$id = $_GET['id'];

//insert values
$sql = "INSERT INTO psu_pharmacies (pharmacy, location) VALUES ('$name','$location')";

if ($conn->query($sql) === TRUE) {
	$pharmacy_id = $conn->insert_id;
	$query = $conn->query("INSERT INTO psu_pharm_directors (psu_id, pharmacy_id) VALUES ('$id','$pharmacy_id')");

    echo "1";
} else {
    echo "0";
}

?>