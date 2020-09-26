package maquis.game;

import maquis.view.BoardPanel;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class AStarAlgorithm {
    public static List<Road> findShortestUnblockedPath(Location start, Location end, Board board){
        return findShortestPath(start, end, board, true);
    }

    public static List<Road> findShortestPath(Location start, Location end, Board board){
        return findShortestPath(start, end, board, false);
    }

    public static List<Road> findShortestPath(Location start, Location end, Board board, boolean reqUnblocked){
        Map<Location, Node> locationNodes = new HashMap<>();
        board.getLocations().stream().forEach(l -> locationNodes.put(l, new Node(l)));

        // Initialize the open list
        Queue<Node> openList = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.f < o2.f? 1: (o1.f > o2.f? -1: 0);
            }
        });
        // Initialize the closed list (visited)
        List<Node> visited = new ArrayList<>();

        // put the starting node on the open list (you can leave its f at zero)
        Node startNode = new Node(start);
        startNode.f = 0;
        openList.add(startNode);

        // while the open list is not empty
        while (!openList.isEmpty()) {
            Node current = openList.remove();

            if (!visited.contains(current)){
                visited.add(current);

                if (current.location.getType() == end.getType())
                    return reconstructPath(startNode, current, board);

                List<Node> neighbors =
                        board.getRoads().stream()
                                .filter(r -> r.hasLocation(current.location) && !r.isBlocked())
                                .map(r -> r.getLocation1() != current.location? locationNodes.get(r.getLocation1()): locationNodes.get(r.getLocation2()))
                                .collect(Collectors.toList());

                for (Node neighbor : neighbors) {
                    if (!visited.contains(neighbor)){
                        // If the location is blocked (ie. by a mission), do not consider it
                        if (neighbor.location.isBlocked()){
                            continue;
                        }
                        // If a location has a Milice/Soldier and we want to avoid them, do not consider it
                        if (reqUnblocked && (neighbor.location.hasMilice() || neighbor.location.hasSoldier())){
                            continue;
                        }
                        // increment hops from start
                        neighbor.hopsFromStart = current.hopsFromStart + 1;

                        // calculate predicted distance to the end node
                        int predictedDistance = calcEstimatedCostToMove(neighbor.location, end, board);

                        // calculate distance to neighbor. 2. calculate dist from start node
                        int totalDistance = calcActualCostToMove(startNode, neighbor) + predictedDistance;

                        // update n's distance
                        neighbor.f = totalDistance;

                        // if a node with the same position as successor is in the OPEN list which has a lower f than successor, skip this successor
                        Optional<Node> betterNode = openList.stream().filter(n -> n.hopsFromStart == neighbor.hopsFromStart && n.f < neighbor.f).findFirst();
                        if (betterNode.isPresent())
                            continue;

                        // if a node with the same position as successor is in the CLOSED list which has a lower f than successor, skip this successor
                        betterNode = visited.stream().filter(n -> n.hopsFromStart == neighbor.hopsFromStart && n.f < neighbor.f).findFirst();
                        if (betterNode.isPresent())
                            continue;

                        // otherwise, add  the node to the open list
                        neighbor.parent = current;
                        openList.add(neighbor);
                    }
                }
            }
        }

        return null;
    }

    private static List<Road> reconstructPath(Node startNode, Node endNode, Board board){
        List<Road> path = new ArrayList<>();
        Node current = endNode;
        while (current.location != startNode.location){
            Node parent = current.parent;
            final Location currentLocation = current.location;
            Road road = board.getRoads().stream().filter(r -> r.hasLocation(currentLocation) && r.hasLocation(parent.location)).findFirst().get();
            path.add(road);
            current = parent;
        }
        Collections.reverse(path);
        return path;
    }

    // Return g
    private static int calcActualCostToMove(Node location1, Node location2){
        int cost = location2.hopsFromStart - location1.hopsFromStart;
        return cost;
    }

    // Return h
    private static int calcEstimatedCostToMove(Location location1, Location location2, Board board){
        if (location1 == location2)
            return 0;
        if (board.getRoads().stream().filter(r -> r.hasLocation(location1) && r.hasLocation(location2)).findAny().isPresent()){
            return 1;
        }
        Point loc1 = BoardPanel.LOCATION_COORD.get(location1.getType());
        Point loc2 = BoardPanel.LOCATION_COORD.get(location2.getType());
        return (int) Math.sqrt(Math.pow(loc1.x - loc2.x, 2) + Math.pow(loc1.y - loc2.y, 2));
    }

    static class Node{
        public Location location;
        public Node parent = null;
        public int hopsFromStart = 0;
        public int f = Integer.MAX_VALUE;

        public Node(Location l){
            this.location = l;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return location.equals(node.location);
        }

        @Override
        public int hashCode() {
            return Objects.hash(location);
        }
    }
}
