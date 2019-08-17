<?php
include 'dbconfig.php';

$id = $_GET['id'];

//update values
$sql = "UPDATE psu_pharmacies SET update_by=NULL, location_set='0' WHERE id = '$id'";

if ($conn->query($sql) === TRUE) {
    echo "1";
} else {
    echo "0";
}

?>