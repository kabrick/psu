<?php
include 'dbconfig.php';

$sql = "SELECT * FROM psu_app_update WHERE id = 1";

$result = $conn->query($sql);

$result_assoc = $result->fetch_assoc();

echo $result_assoc['current_version'];

?>