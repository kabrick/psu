<?php
include 'dbconfig.php';

$id = $_GET['id'];

$sql = "UPDATE psu_forums SET approved = '2' WHERE id = '$id'";

if ($conn->query($sql) === TRUE) {
    echo "1";
} else {
    echo "0";
}

?>