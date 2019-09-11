<?php
include 'dbconfig.php';

$sql = "SELECT admin_id AS id, name, phone AS pharmacy, psu_id FROM psu_admin";

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