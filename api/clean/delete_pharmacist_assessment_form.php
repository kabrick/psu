<?php
include 'dbconfig.php';

$id = $_GET['id'];

$sql = "DELETE FROM psu_pharmacist_assessment WHERE id = '$id'";

if ($conn->query($sql) === TRUE) {
    echo "1";
} else {
    echo "0";
}
?>