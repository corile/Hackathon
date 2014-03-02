// True Price

#ifndef __BUS_INFO_APP__
#define __BUS_INFO_APP__

#include <pebble.h>

#define MAX_NUM_BUSES 20 // get no more than this many buses
#define MAX_LEN_BUS_MENU_STR 20 // maximum text length for a menu label

// Our main window shows the next (selected) bus
static Window *window;
static TextLayer *bus_info_rte_text;
static TextLayer *bus_info_eta_text;
static TextLayer *bus_info_loc_text;

// If you press the select buton, you can select from the list of buses that are
// coming within the next while.
static Window *selector_window;
static MenuLayer *menu_layer;

// structure representing a bus -- route, location, ETA, and menu item (bus
// selection) appearance
typedef struct {
	char *rte, *loc, *eta, menu[MAX_LEN_BUS_MENU_STR];
} BusInfo;

static BusInfo bus_list[MAX_NUM_BUSES]; // our list of buses
static size_t num_buses = 0; // the number of active buses

static size_t selected_bus = MAX_NUM_BUSES; // currently displayed bus
static bool in_menu = false; // are we in the selection screen?

static char last_shortest_time[2]; // keep track of next time for vibration

// window init/unload
static void init(void);
static void window_load(Window *window);
static void window_unload(Window *window);
static void selector_window_load(Window *window);
static void selector_window_unload(Window *window);
static void deinit(void);

// display functions
static void show_bus_info(BusInfo *bus);

// menu display functions
static int16_t get_cell_height_callback(struct MenuLayer *menu_layer,
	MenuIndex *cell_index, void *data);
static uint16_t get_num_rows_callback(struct MenuLayer *menu_layer,
	uint16_t section_index, void *data);
static void draw_row_callback(GContext* ctx, Layer *cell_layer,
	MenuIndex *cell_index, void *data);
static void select_bus_callback(MenuLayer *layer, MenuIndex *cell_index,
	void *data);

// button handling
static void select_click_handler(ClickRecognizerRef recognizer, void *context);
static void up_click_handler(ClickRecognizerRef recognizer, void *context);
static void down_click_handler(ClickRecognizerRef recognizer, void *context);
static void click_config_provider(void *context);

// app message interface
static void app_message_init(void);
static void message_received_handler(DictionaryIterator *iter, void *context);

#endif

