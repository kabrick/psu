<?php
include 'dbconfig.php';

$id = $_GET['id'];

// update values
$sql = "UPDATE psu_news SET approved = '2' WHERE id = '$id'";

if ($conn->query($sql) === TRUE) {
    echo "1";
} else {
    echo "0";
}

?>