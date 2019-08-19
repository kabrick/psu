<?php
include 'dbconfig.php';

$id = $_GET['id'];
$pharmacist = $_GET['pharmacist'];

//update values
$sql = "UPDATE psu_pharmacies SET update_by='$pharmacist' WHERE id = '$id'";

if ($conn->query($sql) === TRUE) {
    echo "1";
} else {
    echo "0";
}

?>