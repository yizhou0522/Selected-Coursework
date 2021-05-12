Challenge 1a -  backwardWarpImg:
This routine converts the source image and warps it on the canvas, given the source image and the product of the source homography. There may be certain areas in the destination during the transition that might not have suited a single pixel due to stretching. The built-in interp2 feature is often used to interpose missing pixels.


Challenge 1b - runRANSAC:
We first used the randperm method to generate appropriate indexes and then utilized computeHomography to estimate the homography between these sets of points. And then using sqrt formula to calculate the distance and compare it with eps value. We keep the maximum value as the final result returned by runRANSAC.


Challenge 1c - blendImagePair:
We employed bwdist function to compute a new weighted mask for blending based on the “euclidean” method. Then we set masks to appropriate value and finally return the output_image based on the weighted masks.


Challenge 1d - stitchImg:
Execute runRANSAC function, backwardWarpImg (used runRANSAC function’s result as inputs), and finally used blendImagePair function to blend the above images, which returns a panorama.


Challenge 1e
Used l.jpg, m.jpg, and r.jpg as inputs, returns a whole_view_panorama.png utilizing stitchImg function.