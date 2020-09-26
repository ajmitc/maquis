package maquis.view;

import maquis.Model;
import maquis.game.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

public class BoardPanel extends JPanel {
    public static final Logger logger = Logger.getLogger(BoardPanel.class.getName());

    public static final int MISSION_AREA_HEIGHT = 100;
    public static final Font MISSION_NAME_FONT = new Font("Serif", Font.BOLD, 16);
    private static final int MISSION_NAME_Y_OFFSET = 50;
    private static final int MISSION_NAME_BORDER = 5;

    private static final Point[] TURN_SPACE_COORDS = {
            new Point(67, 815 + MISSION_AREA_HEIGHT),
            new Point(115, 815 + MISSION_AREA_HEIGHT),
            new Point(160, 815 + MISSION_AREA_HEIGHT),
            new Point(205, 815 + MISSION_AREA_HEIGHT),
            new Point(250, 815 + MISSION_AREA_HEIGHT),
            new Point(295, 815 + MISSION_AREA_HEIGHT),
            new Point(330, 815 + MISSION_AREA_HEIGHT),
            new Point(375, 815 + MISSION_AREA_HEIGHT),
            new Point(420, 815 + MISSION_AREA_HEIGHT),
            new Point(465, 815 + MISSION_AREA_HEIGHT),
            new Point(510, 815 + MISSION_AREA_HEIGHT),
            new Point(555, 815 + MISSION_AREA_HEIGHT),
            new Point(600, 815 + MISSION_AREA_HEIGHT),
            new Point(645, 815 + MISSION_AREA_HEIGHT),
            new Point(690, 815 + MISSION_AREA_HEIGHT),
            new Point(735, 815 + MISSION_AREA_HEIGHT)
    };
    private static final int TURN_MARKER_SIZE = 25;
    private static final Color TURN_MARKER_COLOR = Color.BLUE;

    private static final Point[] SOLDIER_SPACE_COORDS = {
            new Point(935, 815 + MISSION_AREA_HEIGHT),
            new Point(983, 815 + MISSION_AREA_HEIGHT),
            new Point(1030, 815 + MISSION_AREA_HEIGHT),
            new Point(1073, 815 + MISSION_AREA_HEIGHT),
            new Point(1117, 815 + MISSION_AREA_HEIGHT),
            new Point(1165, 815 + MISSION_AREA_HEIGHT)
    };
    private static final int SOLDIER_MARKER_SIZE = 25;
    private static final Color SOLDIER_MARKER_COLOR = Color.RED;

    private static final int FIGURE_SCALE_W = 50;

    private static final Point[] MORALE_SPACE_COORDS = {
            new Point(1094, 353 + MISSION_AREA_HEIGHT),
            new Point(1094, 310 + MISSION_AREA_HEIGHT),
            new Point(1094, 265 + MISSION_AREA_HEIGHT),
            new Point(1094, 224 + MISSION_AREA_HEIGHT),
            new Point(1094, 180 + MISSION_AREA_HEIGHT),
            new Point(1094, 135 + MISSION_AREA_HEIGHT),
            new Point(1094, 92 + MISSION_AREA_HEIGHT),
            new Point(1094, 48 + MISSION_AREA_HEIGHT)
    };
    private static final int MORALE_MARKER_SIZE = 25;
    private static final Color MORALE_MARKER_COLOR = Color.GREEN.darker();

    /**
     * Mapping of Location to list of coordinates to put figures
     * Most locations only need two, but the Safe Houses may need up to 5
     */
    public static final Map<LocationType, List<Point>> LOCATION_FIGURE_COORDS = new HashMap<>();
    static {
        LOCATION_FIGURE_COORDS.put(LocationType.SAFE_HOUSE_1, new ArrayList<>());
        LOCATION_FIGURE_COORDS.get(LocationType.SAFE_HOUSE_1).add(new Point(565, 640 + MISSION_AREA_HEIGHT));
        LOCATION_FIGURE_COORDS.get(LocationType.SAFE_HOUSE_1).add(new Point(610, 650 + MISSION_AREA_HEIGHT));
        LOCATION_FIGURE_COORDS.get(LocationType.SAFE_HOUSE_1).add(new Point(545, 680 + MISSION_AREA_HEIGHT));
        LOCATION_FIGURE_COORDS.get(LocationType.SAFE_HOUSE_1).add(new Point(615, 680 + MISSION_AREA_HEIGHT));
        LOCATION_FIGURE_COORDS.get(LocationType.SAFE_HOUSE_1).add(new Point(600, 700 + MISSION_AREA_HEIGHT));
    }

    private static final int LOCATION_SIZE = 170;
    public static final Map<LocationType, Point> LOCATION_COORD = new HashMap<>();
    static {
        LOCATION_COORD.put(LocationType.SAFE_HOUSE_1, new Point(515, 610 + MISSION_AREA_HEIGHT));
        LOCATION_COORD.put(LocationType.PONT_LEVEQUE, new Point(445, 415 + MISSION_AREA_HEIGHT));
        LOCATION_COORD.put(LocationType.CAFE, new Point(305, 605 + MISSION_AREA_HEIGHT));
        LOCATION_COORD.put(LocationType.FIELD_1, new Point(55, 600 + MISSION_AREA_HEIGHT));
        LOCATION_COORD.put(LocationType.FIELD_2, new Point(735, 610 + MISSION_AREA_HEIGHT));
        LOCATION_COORD.put(LocationType.GROCER, new Point(665, 410 + MISSION_AREA_HEIGHT));
        LOCATION_COORD.put(LocationType.DOCTOR, new Point(230, 225 + MISSION_AREA_HEIGHT));
        LOCATION_COORD.put(LocationType.SPARE_ROOM_1, new Point(225, 420 + MISSION_AREA_HEIGHT));
        LOCATION_COORD.put(LocationType.SPARE_ROOM_2, new Point(890, 245 + MISSION_AREA_HEIGHT));
        LOCATION_COORD.put(LocationType.SPARE_ROOM_3, new Point(880, 450 + MISSION_AREA_HEIGHT));
        LOCATION_COORD.put(LocationType.RADIO_A, new Point(40, 330 + MISSION_AREA_HEIGHT));
        LOCATION_COORD.put(LocationType.RADIO_B, new Point(830, 35 + MISSION_AREA_HEIGHT));
        LOCATION_COORD.put(LocationType.RUE_BARADAT, new Point(40, 115 + MISSION_AREA_HEIGHT));
        LOCATION_COORD.put(LocationType.FENCE, new Point(260, 25 + MISSION_AREA_HEIGHT));
        LOCATION_COORD.put(LocationType.POOR_DISTRICT, new Point(440, 210 + MISSION_AREA_HEIGHT));
        LOCATION_COORD.put(LocationType.BLACK_MARKET, new Point(665, 200 + MISSION_AREA_HEIGHT));
        LOCATION_COORD.put(LocationType.PONT_DU_NORD, new Point(535, 25 + MISSION_AREA_HEIGHT));
        LOCATION_COORD.put(LocationType.MISSION_1, new Point(260, 0 - (182 - MISSION_AREA_HEIGHT)));
        LOCATION_COORD.put(LocationType.MISSION_2, new Point(535, 0 - (182 - MISSION_AREA_HEIGHT)));
    }

    private static final Map<String, Point> ROAD_COORD = new HashMap<>();
    static {
        ROAD_COORD.put(LocationType.BLACK_MARKET.getName() + LocationType.GROCER.getName(), new Point(730, 470));
        ROAD_COORD.put(LocationType.BLACK_MARKET.getName() + LocationType.PONT_DU_NORD.getName(), new Point(680, 285));
        ROAD_COORD.put(LocationType.BLACK_MARKET.getName() + LocationType.POOR_DISTRICT.getName(), new Point(620, 370));
        ROAD_COORD.put(LocationType.BLACK_MARKET.getName() + LocationType.RADIO_B.getName(), new Point(810, 275));
        ROAD_COORD.put(LocationType.BLACK_MARKET.getName() + LocationType.SPARE_ROOM_2.getName(), new Point(850, 375));

        ROAD_COORD.put(LocationType.CAFE.getName() + LocationType.FIELD_1.getName(), new Point(245, 755));
        ROAD_COORD.put(LocationType.CAFE.getName() + LocationType.SAFE_HOUSE_1.getName(), new Point(470, 765));

        ROAD_COORD.put(LocationType.DOCTOR.getName() + LocationType.PONT_LEVEQUE.getName(), new Point(410, 480));
        ROAD_COORD.put(LocationType.DOCTOR.getName() + LocationType.RADIO_A.getName(), new Point(200, 435));
        ROAD_COORD.put(LocationType.DOCTOR.getName() + LocationType.RUE_BARADAT.getName(), new Point(200, 330));
        ROAD_COORD.put(LocationType.DOCTOR.getName() + LocationType.SPARE_ROOM_1.getName(), new Point(295, 490));

        ROAD_COORD.put(LocationType.FENCE.getName() + LocationType.MISSION_1.getName(), new Point(325, 90));
        ROAD_COORD.put(LocationType.FENCE.getName() + LocationType.POOR_DISTRICT.getName(), new Point(415, 280));
        ROAD_COORD.put(LocationType.FENCE.getName() + LocationType.RUE_BARADAT.getName(), new Point(210, 230));

        ROAD_COORD.put(LocationType.GROCER.getName() + LocationType.SAFE_HOUSE_1.getName(), new Point(650, 680));
        ROAD_COORD.put(LocationType.GROCER.getName() + LocationType.SPARE_ROOM_3.getName(), new Point(840, 585));

        ROAD_COORD.put(LocationType.FIELD_2.getName() + LocationType.SAFE_HOUSE_1.getName(), new Point(690, 770));

        ROAD_COORD.put(LocationType.MISSION_2.getName() + LocationType.PONT_DU_NORD.getName(), new Point(605, 95));

        ROAD_COORD.put(LocationType.PONT_DU_NORD.getName() + LocationType.POOR_DISTRICT.getName(), new Point(550, 285));

        ROAD_COORD.put(LocationType.PONT_LEVEQUE.getName() + LocationType.POOR_DISTRICT.getName(), new Point(510, 485));
        ROAD_COORD.put(LocationType.PONT_LEVEQUE.getName() + LocationType.SAFE_HOUSE_1.getName(), new Point(550, 685));

        ROAD_COORD.put(LocationType.RADIO_A.getName() + LocationType.SPARE_ROOM_1.getName(), new Point(195, 535));
        ROAD_COORD.put(LocationType.RADIO_B.getName() + LocationType.SPARE_ROOM_2.getName(), new Point(925, 305));
    }
    private static final int ROAD_SIZE = 25;

    private static final Stroke BLOCKED_STROKE = new BasicStroke(3.f);
    private static final Color BLOCKED_COLOR = Color.RED;

    private static final Point SHOOT_MILICE_COORD = new Point(1085, 420 + MISSION_AREA_HEIGHT);
    private static final int SHOOT_MILICE_SIZE = 55;

    private static final Font RESOURCE_COUNT_FONT    = new Font("Serif", Font.BOLD, 14);
    private static final int RESOURCE_COUNT_BORDER   = 5;
    private static final Point WEAPON_COUNT_COORD    = new Point(1090, 436 + MISSION_AREA_HEIGHT);
    private static final Point MONEY_COUNT_COORD     = new Point(1090, 496 + MISSION_AREA_HEIGHT);
    private static final Point FOOD_COUNT_COORD      = new Point(1090, 556 + MISSION_AREA_HEIGHT);
    private static final Point INTEL_COUNT_COORD     = new Point(1090, 620 + MISSION_AREA_HEIGHT);
    private static final Point MEDICINE_COUNT_COORD  = new Point(1090, 676 + MISSION_AREA_HEIGHT);
    private static final Point EXPLOSIVE_COUNT_COORD = new Point(1090, 735 + MISSION_AREA_HEIGHT);

    private static final int RESOURCE_ICON_SIZE = 54;

    private static final int RESTART_ICON_SIZE = 40;
    private static final int RESTART_ICON_X = 1200 - RESTART_ICON_SIZE;
    private static final int RESTART_ICON_Y = MISSION_AREA_HEIGHT - RESTART_ICON_SIZE;

    private static final int UNDO_ICON_GAP  = 10;
    private static final int UNDO_ICON_SIZE = 40;
    private static final int UNDO_ICON_X = 1200 - RESTART_ICON_SIZE - UNDO_ICON_SIZE - UNDO_ICON_GAP;
    private static final int UNDO_ICON_Y = MISSION_AREA_HEIGHT - UNDO_ICON_SIZE;

    private Model model;
    private View view;
    private Image boardImage;
    private int mouseX, mouseY;

    public BoardPanel(Model model, View view){
        super();
        this.model = model;
        this.view = view;

        boardImage = ImageUtil.load("board.png");

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                mouseX = e.getX();
                mouseY = e.getY();
                refresh();
            }
        });
    }

    public Location getLocationAt(int mx, int my){
        for (LocationType locationType: LOCATION_COORD.keySet()){
            Point p = LOCATION_COORD.get(locationType);
            if (mx >= p.x && mx < p.x + LOCATION_SIZE && my >= p.y && my < p.y + LOCATION_SIZE){
                return model.getGame().getBoard().getLocation(locationType);
            }
        }
        return null;
    }

    public boolean inShootMiliceActionArea(int mx, int my){
        return mx >= SHOOT_MILICE_COORD.x && mx < SHOOT_MILICE_COORD.x + SHOOT_MILICE_SIZE &&
                my >= SHOOT_MILICE_COORD.y && my < SHOOT_MILICE_COORD.y + SHOOT_MILICE_SIZE;
    }

    public boolean inUndoActionArea(int mx, int my){
        return mx >= UNDO_ICON_X && mx < UNDO_ICON_X + UNDO_ICON_SIZE &&
                my >= UNDO_ICON_Y && my < UNDO_ICON_Y + UNDO_ICON_SIZE;
    }

    public boolean inRestartActionArea(int mx, int my){
        return mx >= RESTART_ICON_X && mx < RESTART_ICON_X + RESTART_ICON_SIZE &&
                my >= RESTART_ICON_Y && my < RESTART_ICON_Y + RESTART_ICON_SIZE;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        // Draw Mission Locations
        Image mission1location = ImageUtil.load("mission1location.png");
        Image mission2location = ImageUtil.load("mission2location.png");
        Point mission1Pt = LOCATION_COORD.get(LocationType.MISSION_1);
        Point mission2Pt = LOCATION_COORD.get(LocationType.MISSION_2);
        g2d.drawImage(mission1location, mission1Pt.x, mission1Pt.y, null);
        g2d.drawImage(mission2location, mission2Pt.x, mission2Pt.y, null);

        Font oldFont = g2d.getFont();
        g2d.setFont(MISSION_NAME_FONT);
        Rectangle mission1NameBounds = ViewUtil.getStringBounds(g2d, model.getGame().getMission1().getName(), mission1Pt.x, MISSION_NAME_Y_OFFSET);
        Rectangle mission2NameBounds = ViewUtil.getStringBounds(g2d, model.getGame().getMission2().getName(), mission2Pt.x, MISSION_NAME_Y_OFFSET);
        mission1NameBounds.grow(MISSION_NAME_BORDER, MISSION_NAME_BORDER);
        mission2NameBounds.grow(MISSION_NAME_BORDER, MISSION_NAME_BORDER);

        g2d.setColor(Color.WHITE);
        g2d.fillRect(mission1NameBounds.x, mission1NameBounds.y, mission1NameBounds.width, mission1NameBounds.height);
        g2d.fillRect(mission2NameBounds.x, mission2NameBounds.y, mission2NameBounds.width, mission2NameBounds.height);
        g2d.setColor(Color.BLACK);
        g2d.drawString(model.getGame().getMission1().getName(), mission1NameBounds.x + MISSION_NAME_BORDER, mission1NameBounds.y + mission1NameBounds.height - MISSION_NAME_BORDER);
        g2d.drawString(model.getGame().getMission2().getName(), mission2NameBounds.x + MISSION_NAME_BORDER, mission2NameBounds.y + mission2NameBounds.height - MISSION_NAME_BORDER);
        g2d.setFont(oldFont);

        g2d.drawImage(boardImage, 0, MISSION_AREA_HEIGHT, null);

        // Draw restart and undo icons
        drawRestartIcon(g2d);
        drawUndoIcon(g2d);

        // Draw Spare Rooms
        drawSpareRooms(g2d);

        // Draw markers
        drawTurnMarker(g2d);
        drawSoldierMarker(g2d);
        drawMoraleMarker(g2d);

        // Draw Resource Counts
        drawResourceCounts(g2d);

        // TODO Draw blocked locations and roads (put an X on the location/road)
        drawBlockedLocationsAndRoads(g2d);

        // Draw Field Resources
        drawFieldResources(g2d);

        // Draw figures
        drawFigures(g2d);

        // Internal only
        g2d.setColor(Color.BLACK);
        g2d.drawString(mouseX + ", " + mouseY, 30, MISSION_AREA_HEIGHT + 30);
//        for (Point p: LOCATION_COORD.values()){
//            g2d.drawRect(p.x, p.y, LOCATION_SIZE, LOCATION_SIZE);
//        }
        //g2d.drawRect(SHOOT_MILICE_COORD.x, SHOOT_MILICE_COORD.y, SHOOT_MILICE_SIZE, SHOOT_MILICE_SIZE);
    }

    private void drawSpareRooms(Graphics2D g){
        model.getGame().getBoard().getLocations().stream()
                .filter(l -> l.getType().isSpareRoom() && l.getSpareRoomType() != null)
                .forEach(l -> drawSpareRoom(g, l));
    }

    private void drawSpareRoom(Graphics2D g, Location location) {
        Point p = LOCATION_COORD.get(location.getType());
        Image image;
        switch(location.getSpareRoomType()){
            case CHEMISTS_LAB:
                image = ImageUtil.load("chemistlab.jpg", LOCATION_SIZE);
                break;
            case COUNTERFEITER:
                image = ImageUtil.load("counterfeiter.jpg", LOCATION_SIZE);
                break;
            case SMUGGLER:
                image = ImageUtil.load("smuggler.jpg", LOCATION_SIZE);
                break;
            case SAFE_HOUSE_2:
                image = ImageUtil.load("safe_house.jpg", LOCATION_SIZE);
                break;
            case INFORMANT:
                image = ImageUtil.load("informant.jpg", LOCATION_SIZE);
                break;
            case PROPAGANDIST:
                image = ImageUtil.load("propagandist.jpg", LOCATION_SIZE);
                break;
            default:
                logger.warning("Invalid spare room type: " + location.getSpareRoomType());
                return;
        }
        g.drawImage(image, p.x, p.y, null);
//        String name = location.getSpareRoomType().getName();
//        Rectangle nameRect = ViewUtil.getStringBounds(g, name, p.x + LOCATION_NAME_XOFFSET, p.y + LOCATION_NAME_YOFFSET);
//        nameRect.grow(5, 5);
//        g.setColor(Color.WHITE);
//        g.fillRect(nameRect.x, nameRect.y, nameRect.width, nameRect.height);
//        g.setColor(Color.BLACK);
//        g.drawString(name, p.x + LOCATION_NAME_XOFFSET, p.y + LOCATION_NAME_YOFFSET);
    }

    private void drawFigures(Graphics2D g){
        for (Location location: model.getGame().getBoard().getLocations()){
            drawFigures(g, location);
        }
    }

    private void drawFigures(Graphics2D g, Location location){
        Point point = LOCATION_COORD.get(location.getType());
        int cx = point.x + (LOCATION_SIZE / 2);
        int cy = point.y + (LOCATION_SIZE / 2);
        if (!location.getAgents().isEmpty()){
            for (Agent agent: location.getAgents()){
                if (agent.isRecruited() && !agent.isArrested())
                    drawAgent(g, agent);
            }
        }
        if (location.hasMilice()){
            drawMilice(g, cx, cy);
        }
        if (location.hasSoldier()){
            drawSoldier(g, cx, cy);
        }
    }

    private void drawAgent(Graphics2D g, Agent agent){
        Image bi = ImageUtil.load("agent" + (agent.isSelected()? "-selected": "-green") + ".png", FIGURE_SCALE_W);
        g.drawImage(bi, agent.getX(), agent.getY(), null);
    }

    private void drawMilice(Graphics2D g, int x, int y){
        Image bi = ImageUtil.load("milice.png", FIGURE_SCALE_W);
        g.drawImage(bi, x, y, null);
    }

    private void drawSoldier(Graphics2D g, int x, int y){
        Image bi = ImageUtil.load("soldier.png", FIGURE_SCALE_W);
        g.drawImage(bi, x, y, null);
    }

    private void drawTurnMarker(Graphics2D g){
        Point p = TURN_SPACE_COORDS[model.getGame().getTurn() - 1];
        g.setColor(TURN_MARKER_COLOR);
        //g.fillRoundRect(p.x, p.y, TURN_MARKER_SIZE, TURN_MARKER_SIZE, 5, 5);
        g.fill3DRect(p.x, p.y, TURN_MARKER_SIZE, TURN_MARKER_SIZE, true);
    }

    private void drawSoldierMarker(Graphics2D g){
        Point p = SOLDIER_SPACE_COORDS[model.getGame().getSoldiers()];
        g.setColor(SOLDIER_MARKER_COLOR);
        //g.fillRoundRect(p.x, p.y, SOLDIER_MARKER_SIZE, SOLDIER_MARKER_SIZE, 5, 5);
        g.fill3DRect(p.x, p.y, SOLDIER_MARKER_SIZE, SOLDIER_MARKER_SIZE, true);
    }

    private void drawMoraleMarker(Graphics2D g){
        Point p = MORALE_SPACE_COORDS[model.getGame().getMorale()];
        g.setColor(MORALE_MARKER_COLOR);
        //g.fillRoundRect(p.x, p.y, MORALE_MARKER_SIZE, MORALE_MARKER_SIZE, 5, 5);
        g.fill3DRect(p.x, p.y, MORALE_MARKER_SIZE, MORALE_MARKER_SIZE, true);
    }

    private void drawResourceCounts(Graphics2D g){
        Font oldFont = g.getFont();
        g.setFont(RESOURCE_COUNT_FONT);
        drawResourceCount(g, (int) model.getGame().getResources().stream().filter(r -> r == Resource.WEAPONS).count(), WEAPON_COUNT_COORD);
        drawResourceCount(g, (int) model.getGame().getResources().stream().filter(r -> r == Resource.MONEY).count(), MONEY_COUNT_COORD);
        drawResourceCount(g, (int) model.getGame().getResources().stream().filter(r -> r == Resource.FOOD).count(), FOOD_COUNT_COORD);
        drawResourceCount(g, (int) model.getGame().getResources().stream().filter(r -> r == Resource.INTEL).count(), INTEL_COUNT_COORD);
        drawResourceCount(g, (int) model.getGame().getResources().stream().filter(r -> r == Resource.MEDICINE).count(), MEDICINE_COUNT_COORD);
        drawResourceCount(g, (int) model.getGame().getResources().stream().filter(r -> r == Resource.EXPLOSIVES).count(), EXPLOSIVE_COUNT_COORD);
        g.setFont(oldFont);
    }

    private void drawResourceCount(Graphics2D g, int count, Point p){
        Rectangle stringBounds = ViewUtil.getStringBounds(g, "" + count, p.x, p.y);
        stringBounds.grow(RESOURCE_COUNT_BORDER, RESOURCE_COUNT_BORDER);
        g.setColor(Color.WHITE);
        g.fillRect(stringBounds.x, stringBounds.y, stringBounds.width, stringBounds.height);
        g.setColor(Color.BLACK);
        g.drawString("" + count, stringBounds.x + RESOURCE_COUNT_BORDER, stringBounds.y + stringBounds.height - RESOURCE_COUNT_BORDER);
    }

    private void drawFieldResources(Graphics2D g){
        FieldLocation field1 = (FieldLocation) model.getGame().getBoard().getLocation(LocationType.FIELD_1);
        FieldLocation field2 = (FieldLocation) model.getGame().getBoard().getLocation(LocationType.FIELD_2);

        Point field1Loc = LOCATION_COORD.get(LocationType.FIELD_1);
        Point field2Loc = LOCATION_COORD.get(LocationType.FIELD_2);

        Point field1LocCenter = new Point(field1Loc.x + (LOCATION_SIZE / 2) - (RESOURCE_ICON_SIZE / 2), field1Loc.y + (LOCATION_SIZE / 2) - (RESOURCE_ICON_SIZE / 2));
        Point field2LocCenter = new Point(field2Loc.x + (LOCATION_SIZE / 2) - (RESOURCE_ICON_SIZE / 2), field2Loc.y + (LOCATION_SIZE / 2) - (RESOURCE_ICON_SIZE / 2));

        for (Resource resource: field1.getResources()){
            Image bi = ImageUtil.load(resource.getFilename());
            g.drawImage(bi, field1LocCenter.x, field1LocCenter.y, null);
        }

        for (Resource resource: field2.getResources()){
            Image bi = ImageUtil.load(resource.getFilename());
            g.drawImage(bi, field2LocCenter.x, field2LocCenter.y, null);
        }
    }

    private void drawBlockedLocationsAndRoads(Graphics2D g){
        Stroke oldStroke = g.getStroke();
        g.setStroke(BLOCKED_STROKE);
        g.setColor(BLOCKED_COLOR);
        model.getGame().getBoard().getLocations().stream().filter(l -> l.isBlocked()).forEach(l -> {
            Point p = LOCATION_COORD.get(l.getType());
            g.drawLine(p.x, p.y, p.x + LOCATION_SIZE, p.y + LOCATION_SIZE);
            g.drawLine(p.x + LOCATION_SIZE, p.y, p.x, p.y + LOCATION_SIZE);
        });

        model.getGame().getBoard().getRoads().stream().filter(r -> r.isBlocked()).forEach(r -> {
            List<String> locTypes = Arrays.asList(r.getLocation1().getType().getName(), r.getLocation2().getType().getName());
            locTypes.sort(String::compareTo);
            Point p = ROAD_COORD.get(locTypes.get(0) + locTypes.get(1));
            g.drawLine(p.x, p.y, p.x + ROAD_SIZE, p.y + ROAD_SIZE);
            g.drawLine(p.x + ROAD_SIZE, p.y, p.x, p.y + ROAD_SIZE);
        });
        g.setStroke(oldStroke);
        g.setColor(Color.BLACK);
    }

    private void drawRestartIcon(Graphics2D g){
        g.drawImage(ImageUtil.load("restart-icon.png", RESTART_ICON_SIZE), RESTART_ICON_X, RESTART_ICON_Y, null);
    }

    private void drawUndoIcon(Graphics2D g){
        g.drawImage(ImageUtil.load("undo-icon.png", UNDO_ICON_SIZE), UNDO_ICON_X, UNDO_ICON_Y, null);
    }

    public void refresh(){
        repaint();
    }
}
