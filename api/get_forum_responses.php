<?php
include 'dbconfig.php';

$id = $_GET['id'];

$sql = "SELECT c.psu_id AS id, c.comment, c.timestamp, a.name AS author FROM psu_admin a, psu_forum_responses c WHERE c.topic_id = '$id' AND c.psu_id = a.psu_id ORDER BY c.id ASC";

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