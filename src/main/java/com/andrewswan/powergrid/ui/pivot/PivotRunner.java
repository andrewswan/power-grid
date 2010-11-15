package com.andrewswan.powergrid.ui.pivot;

import static com.andrewswan.powergrid.domain.Player.Colour.BLACK;
import static com.andrewswan.powergrid.domain.Player.Colour.BLUE;
import static com.andrewswan.powergrid.domain.Player.Colour.GREEN;
import static com.andrewswan.powergrid.domain.Player.Colour.PURPLE;
import static com.andrewswan.powergrid.domain.Player.Colour.RED;
import static com.andrewswan.powergrid.domain.Player.Colour.YELLOW;

import java.text.Collator;
import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.List;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.ImageView;
import org.apache.pivot.wtk.ListButton;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.TablePane;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;
import org.apache.pivot.wtk.TablePane.RowSequence;
import org.apache.pivot.wtkx.WTKX;
import org.apache.pivot.wtkx.WTKXSerializer;

import com.andrewswan.powergrid.domain.Player;
import com.andrewswan.powergrid.domain.PlayerStrategy;
import com.andrewswan.powergrid.domain.service.BoardService;
import com.andrewswan.powergrid.domain.service.PlayerService;
import com.andrewswan.powergrid.domain.service.impl.BoardServiceImpl;
import com.andrewswan.powergrid.domain.service.impl.PlayerServiceImpl;

/**
 * Runs this application with the Apache Pivot UI
 * 
 * @author Admin
 */
public class PivotRunner implements Application {

    // Constants
    protected static final Log LOGGER = LogFactory.getLog(PivotRunner.class);
    
    private static final String CHARACTER_SET = "UTF-8";
    
    /**
     * Command-line property to specify the ISO language code. Note that all
     * such properties must be prefixed with "--", e.g. "--language=fr".
     */
    public static final String LANGUAGE_PROPERTY_NAME = "language";
    
    /**
     * @param args
     */
    public static void main(final String[] args) {
        DesktopApplicationContext.main(PivotRunner.class, args);
    }

    // Properties
    private final java.util.Map<Class<? extends PlayerStrategy>, String> aiStrategies;
    private final BoardService boardService;
    private final PlayerService playerService;
    
    private Resources translations;
    private Window window;
    
    @WTKX private ListButton btnMap;
    @WTKX private PushButton btnQuit;
    @WTKX private PushButton btnStartGame;
    @WTKX private TablePane tblPlayers;
    
    /**
     * Constructor
     *
     */
    public PivotRunner() {
        // TODO inject these
        this.aiStrategies =
            new HashMap<Class<? extends PlayerStrategy>, String>();
        this.boardService = new BoardServiceImpl();
        this.playerService = new PlayerServiceImpl();
    }

    @Override
    public void startup(
            final Display display, final Map<String, String> properties)
        throws Exception
    {
        // Set the desired locale and use it to load the translations
        String language = properties.get(LANGUAGE_PROPERTY_NAME);
        if (language != null) {
            Locale.setDefault(new Locale(language));
        }
        this.translations = new Resources(getClass().getName(), CHARACTER_SET);

        // Load and bind to the WTKX source
        final WTKXSerializer wtkxSerializer = new WTKXSerializer(translations);
        window = (Window) wtkxSerializer.readObject(this, "new_game.wtkx");
        wtkxSerializer.bind(this);  // sets fields annotated with @WTKX
        initialiseDynamicContent();
        initialiseEventHandlers();
        window.open(display);
    }

    /**
     * Initialises the dynamic UI content
     */
    private void initialiseDynamicContent() {
        initialiseBoardNames();
        initialisePlayerDetails();
    }

    /**
     * @param translations
     */
    private void initialiseBoardNames() {
        LOGGER.debug("btnMap is a " + btnMap.getClass().getName());
        final Collator collator = Collator.getInstance(Locale.getDefault());
        final List<Object> boardNames = new ArrayList<Object>(collator);
        for (final String boardNameCode : boardService.getBoardNameCodes()) {
            boardNames.add(translations.getString(boardNameCode));
        }
        btnMap.setListData(boardNames);
        btnMap.setSelectedIndex(0);
    }
    
    private static final java.util.Map<Player.Colour, String>
        PLAYER_COLOUR_IMAGES = new HashMap<Player.Colour, String>()
    {
        {
            put(BLACK, "images/black_house.png");
            put(BLUE, "images/blue_house.png");
            put(GREEN, "images/green_house.png");
            put(PURPLE, "images/purple_house.png");
            put(RED, "images/red_house.png");
            put(YELLOW, "images/yellow_house.png");
        }
    };
    
    private void initialisePlayerDetails() {
        final RowSequence rows = tblPlayers.getRows();
        for (final Player.Colour colour : Player.Colour.values()) {
            final TablePane.Row row = new TablePane.Row();
            // Colour
            final ImageView imageView = new ImageView();
            final String imagePath = PLAYER_COLOUR_IMAGES.get(colour);
            imageView.setImage(getClass().getResource(imagePath));
            row.add(imageView);
            
            // Player Type
            final List<Object> playerTypes = new ArrayList<Object>();
            playerTypes.add(translations.get("none"));
            playerTypes.add(translations.get("human"));
            this.aiStrategies.putAll(playerService.getComputerPlayerStrategies());
            for (final String aiTypeCode : aiStrategies.values()) {
                playerTypes.add(translations.get(aiTypeCode));
            }
            final ListButton playerTypeButton = new ListButton(playerTypes);
            playerTypeButton.setSelectedIndex(0);
            row.add(playerTypeButton);
            
            // Player Name
            final TextInput nameField = new TextInput();
            final String colourKey = colour.name().toLowerCase();
            nameField.setText(translations.getString(colourKey));
            row.add(nameField);
            
            rows.add(row);
        }
    }
    
    /**
     * Wires up the event handlers
     */
    private void initialiseEventHandlers() {
        btnQuit.getButtonPressListeners().add(new ButtonPressListener() {
            
            @Override
            public void buttonPressed(final Button button) {
                System.exit(0);
            }
        });
        
        btnStartGame.getButtonPressListeners().add(new ButtonPressListener() {
            
            @Override
            public void buttonPressed(final Button button) {
                startGame();
            }
        });
    }
    
    /**
     * Starts a game of Power Grid using the user's entered parameters
     */
    private void startGame() {
        LOGGER.debug("User clicked start game");
        final Object mapItem = btnMap.getSelectedItem();
        LOGGER.debug("Selected map = " + mapItem +
                " (" + mapItem.getClass().getName() + ")");
    }
    
    // ------------------ Pivot Application lifecycle methods ------------------
    
    @Override
    public boolean shutdown(final boolean optional) {
        if (this.window != null) {
            window.close();
        }
        return false;
    }

    @Override
    public void suspend() {}
    
    @Override
    public void resume() {}
}
