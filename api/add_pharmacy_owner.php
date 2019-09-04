<?php
include 'dbconfig.php';

$pharmacist = $_GET['pharmacist'];
$pharmacy_id = $_GET['id'];

//insert values
$sql = "INSERT INTO psu_pharm_directors (psu_id, pharmacy_id) VALUES ('$pharmacist','$pharmacy_id')";

if ($conn->query($sql) === TRUE) {
	echo "1";
} else {
    echo "0";
}

?>