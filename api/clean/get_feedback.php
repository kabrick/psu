<?php
include 'dbconfig.php';

$sql = "SELECT id, text, title FROM psu_feedback ORDER BY id DESC";

$result = $conn->query($sql);

if ($result->num_rows > 0) {
	while($row[] = $result->fetch_assoc()) {
 		$json = json_encode($row);
 	}
} else {
	//
}

echo $json;

?>