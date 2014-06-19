#image-search
============

This is a Google Image Search android app that will allow user to modify advanced search options and result pagination.

Time spent: ?

Completed user stories:

 * [x] User can enter a search query that will display a grid of image results from the Google Image API.
 * [x] User can click on "settings" which allows selection of advanced search options to filter results
 * User can configure advanced search filters such as:
     * [x] Size (small, medium, large, extra-large)
     * [x] Color filter (black, blue, brown, gray, green, etc...)
     * [x] Type (faces, photo, clip art, line art)
     * [x] Site (espn.com)
 * [x] Subsequent searches will have any filters applied to the search results
 * [x] User can tap on any image in results to see the image full-screen
 * [x] User can scroll down “infinitely” to continue loading more image results (up to 8 pages)
 * [x] Optional: Use the ActionBar SearchView or custom layout as the query box instead of an EditText
 * [] Optional: User can share an image to their friends or email it to themselves
 * [] Optional: Robust error handling, check if internet is available, handle error cases, network failures
 * [x] Optional: Improve the user interface and experiment with image assets and/or styling and coloring
 * [x] Stretch: Replace Filter Settings Activity with a lightweight modal overlay
 * [] Stretch: User can zoom or pan images displayed in full-screen detail view
 * [x] Stretch: Use the StaggeredGridView to display visually interesting image results
    * StaggeredGridView does not behave with infinite scroll
 
Tips:

* Etsy's AndroidStaggeredGrid does not behave with endless scroling.
    * gridview item does not respond to click after a user scroll.
    * scrolling  up does not work
* Loading images asynchronously in gridView
    * Must declared fixed size for each grid view item.  DO NOT use "wrap_content" in R.layout.adapter_item.xml, else Gridview will never finish loading.

Play with following libraries:
* ParcelableGenerator to auto generate boiler code for Parcelable classes
* SmartImageView for loading image from url
* Etsy's AndroidStaggeredGrid for multi-column staggered grids
* ActionBarSherlock for backward compatibility.
* Picasso

Walkthrough of all user stories:

![Video Walkthrough](imageSearch.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).


