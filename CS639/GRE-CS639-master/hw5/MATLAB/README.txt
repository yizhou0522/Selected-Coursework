Compute Flow:
We utilized the normxcorr2 function and mainly follows the steps showed in the assignment:
1. Extract the template and search_window from img1 and img2
2. Find the location in the search_window that best matches the template. Recall that
normalized cross-correlation is what we use in template matching to measure how
well the template matches.
3.Given the location of the best match, store the optical flow (motion in x and y).
The debug1a result is a collection of horizontal flow fields.


Tracking: 
We loaded the obj_roi by the given get_ROI function and used rgb2ind function to transform the image such that it only has a fixed number of colors. After finding the initial positions of col, row, width, and height, we also need to extract the area which we will search for the object. By searching the histogram derived from the previous step, we can get the updated obj_row and obj_col and reach the final result.