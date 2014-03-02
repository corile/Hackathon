// True Price

#include <pebble.h>
#include "test.h"

/*****************************************************************************/
// window init/unload

// initialize everything
static void init(void) {
	// main window
  window = window_create();
  window_set_click_config_provider(window, click_config_provider);
  window_set_window_handlers(window, (WindowHandlers) {
    .load = window_load,
    .unload = window_unload,
  });
  const bool animated = true;
  window_stack_push(window, animated);

	// route selection window
  selector_window = window_create();
  window_set_click_config_provider(selector_window, click_config_provider);
  window_set_window_handlers(selector_window, (WindowHandlers) {
    .load = selector_window_load,
    .unload = selector_window_unload,
  });
}

// main window onload
static void window_load(Window *window) {
  Layer *window_layer = window_get_root_layer(window);
  GRect bounds = layer_get_bounds(window_layer);

	// three text fields
  bus_info_rte_text = text_layer_create((GRect) {
		.origin = { 0, 10},
		.size = { bounds.size.w, bounds.size.h / 3}
	});
  bus_info_eta_text = text_layer_create((GRect) {
		.origin = { 0, bounds.size.h / 3 - 5},
		.size = { bounds.size.w, bounds.size.h / 3}
	});
  bus_info_loc_text = text_layer_create((GRect) {
		.origin = { 0, 2 * bounds.size.h / 3 },
		.size = { bounds.size.w, bounds.size.h / 3}
	});

	// should probably declare this string elsewhere.
  text_layer_set_text(bus_info_eta_text, "No routes found.");

  text_layer_set_text_alignment(bus_info_rte_text, GTextAlignmentCenter);
  text_layer_set_text_alignment(bus_info_eta_text, GTextAlignmentCenter);
  text_layer_set_text_alignment(bus_info_loc_text, GTextAlignmentCenter);

  layer_add_child(window_layer, text_layer_get_layer(bus_info_rte_text));
  layer_add_child(window_layer, text_layer_get_layer(bus_info_eta_text));
  layer_add_child(window_layer, text_layer_get_layer(bus_info_loc_text));
}

// destroy all the things
static void window_unload(Window *window) {
  text_layer_destroy(bus_info_rte_text);
  text_layer_destroy(bus_info_eta_text);
  text_layer_destroy(bus_info_loc_text);
}

// selection menu onload - basically taken from examples
static void selector_window_load(Window *window) {
  Layer *window_layer = window_get_root_layer(window);
  GRect window_frame = layer_get_frame(window_layer);
	
  menu_layer = menu_layer_create(window_frame);
  menu_layer_set_callbacks(menu_layer, NULL, (MenuLayerCallbacks) {
    .get_cell_height = (MenuLayerGetCellHeightCallback) get_cell_height_callback,
    .draw_row = (MenuLayerDrawRowCallback) draw_row_callback,
    .get_num_rows = (MenuLayerGetNumberOfRowsInSectionsCallback) get_num_rows_callback,
		.select_click = (MenuLayerSelectCallback) select_bus_callback,
//    .select_long_click = (MenuLayerSelectCallback) select_long_callback
  });
  menu_layer_set_click_config_onto_window(menu_layer, window);
  layer_add_child(window_layer, menu_layer_get_layer(menu_layer));
}

// destroy the rest of the things
static void selector_window_unload(Window *window) {
	menu_layer_destroy(menu_layer);
}

// destroy the world
static void deinit(void) {
  window_destroy(window);
  window_destroy(selector_window);
}

/*****************************************************************************/
// display functions

static void show_bus_info(BusInfo *bus) {
	if(num_buses > 0) {
		text_layer_set_font(bus_info_rte_text,
			fonts_get_system_font(FONT_KEY_GOTHIC_28_BOLD));
		text_layer_set_text(bus_info_rte_text, bus->rte);

		//snprintf(bus->eta_str, MAX_LEN_BUS_ETA_STR, "%li", bus->eta);
		//time_t now = time(NULL);
		//const time_t diff = (now < bus->eta) ? bus->eta - 1000 : 0;//now : 0;
		//struct tm *timediff = localtime(&diff);
		//strftime(bus->eta_str, MAX_LEN_BUS_ETA_STR, "%Ss", timediff);
		text_layer_set_font(bus_info_eta_text,
			fonts_get_system_font(FONT_KEY_BITHAM_42_BOLD));
		text_layer_set_text(bus_info_eta_text, bus->eta);

		text_layer_set_font(bus_info_loc_text,
			fonts_get_system_font(FONT_KEY_GOTHIC_28_BOLD));
		text_layer_set_text(bus_info_loc_text, bus->loc);
	} else {
		text_layer_set_text(bus_info_rte_text, "");
		text_layer_set_font(bus_info_eta_text,
			fonts_get_system_font(FONT_KEY_GOTHIC_14));
		text_layer_set_text(bus_info_eta_text, "No routes found.");
		text_layer_set_text(bus_info_loc_text, "");
	}
}

/*****************************************************************************/
// menu display functions

// height of menu items; unashamedly hard-coded. Actually, some shame.
static int16_t get_cell_height_callback(struct MenuLayer *menu_layer,
	MenuIndex *cell_index, void *data) {
  return 26;
}

// get number of menu rows
static uint16_t get_num_rows_callback(struct MenuLayer *menu_layer,
	uint16_t section_index, void *data) {
  return num_buses;
}

// render selection menu items
static void draw_row_callback(GContext* ctx, Layer *cell_layer,
	MenuIndex *cell_index, void *data) {
  const size_t i = cell_index->row;

	if(i < num_buses) {
		menu_cell_basic_draw(ctx, cell_layer, bus_list[i].menu, NULL, NULL);
	}
}

// when a bus is selected, show it on the main screen
static void select_bus_callback(MenuLayer *layer, MenuIndex *cell_index,
	void *data) {
  const size_t i = cell_index->row;

	if(i < num_buses) { // select current bus and pop the selection window
		selected_bus = i;
		show_bus_info(&bus_list[i]);
		in_menu = false;
		const bool animated = true;
		window_stack_pop(animated); // leave selection screen
	}
}

/*****************************************************************************/
// button handling

static void select_click_handler(ClickRecognizerRef recognizer, void *context) {
	if(num_buses > 0) { // open bus selection menu
		window_stack_push(selector_window, true);
		in_menu = true;
	}
}

static void up_click_handler(ClickRecognizerRef recognizer, void *context) {
	// stub
}

static void down_click_handler(ClickRecognizerRef recognizer, void *context) {
	// stub
}

// set up all the button stuff
static void click_config_provider(void *context) {
  window_single_click_subscribe(BUTTON_ID_SELECT, select_click_handler);
  window_single_click_subscribe(BUTTON_ID_UP, up_click_handler);
  window_single_click_subscribe(BUTTON_ID_DOWN, down_click_handler);
}

/*****************************************************************************/
// messaging with Android app

// init app message passing (receiving) interface - taken from examples, mostly
static void app_message_init(void) {
  // Reduce the sniff interval for more responsive messaging at the expense of
  // increased energy consumption by the Bluetooth module
  // The sniff interval will be restored by the system after the app has been
  // unloaded
  app_comm_set_sniff_interval(SNIFF_INTERVAL_REDUCED);

  app_message_open(100, 16); // init message buffers
  // Register message handlers
  app_message_register_inbox_received(message_received_handler);
}

// when the app sends a message, do this
static void message_received_handler(DictionaryIterator *iter, void *context) {
	Tuple *bus_info_tuple;
	char *bus_info; // for convenience of readability
	size_t len; // ditto
	char *sel_name = ""; // get currently selected bus; search for it

	num_buses = 0; // reset number of buses
	if(selected_bus < MAX_NUM_BUSES) { // get current bus route name
		len = strlen(bus_list[selected_bus].rte);
		sel_name = (char *)malloc(len);
		strcpy(sel_name, bus_list[selected_bus].rte);
	}
	selected_bus = MAX_NUM_BUSES;

	// repeatedly pull buses until none are left
	while((bus_info_tuple = dict_find(iter, num_buses)) != NULL &&
			num_buses < MAX_NUM_BUSES) {
		bus_info = bus_info_tuple->value->cstring;
		bus_list[num_buses].eta = bus_info;
		len = strlen(bus_info) + 1;
		bus_list[num_buses].rte = (bus_info + len);
		len += strlen(bus_list[num_buses].rte) + 1;
		bus_list[num_buses].loc = (bus_info + len);
		snprintf(bus_list[num_buses].menu, MAX_LEN_BUS_MENU_STR, "%s (%s)",
			bus_list[num_buses].rte, bus_list[num_buses].eta);

		// is this our current bus? (ignore duplicates)
		if(strcmp(sel_name, bus_list[num_buses].rte) == 0) {
			selected_bus = num_buses;
		}

		++num_buses;
	}

  free(sel_name);

	if(selected_bus == MAX_NUM_BUSES) // our bus disappeared!
		selected_bus = 0;

	// show menu or current bus
	if(in_menu && num_buses > 0) {
		menu_layer_reload_data(menu_layer);
	} else {
		show_bus_info(&bus_list[selected_bus]);
	}

	// vibrate at five minutes
	if(strcmp(bus_list[selected_bus].eta, "5m") == 0 &&
		strcmp(last_shortest_time, "6m") == 0) {
		vibes_double_pulse();
	}

	last_shortest_time[0] = bus_list[selected_bus].eta[0];
	last_shortest_time[1] = bus_list[selected_bus].eta[1];
}

/*****************************************************************************/
// main function

int main(void) {
  init(); // init window

  app_message_init(); // init message passing

  app_event_loop();
  deinit();
}

