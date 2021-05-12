function [mask, result_img] = backwardWarpImg(src_img, resultToSrc_H,...
                                              dest_canvas_width_height)
	src_height = size(src_img, 1);
	src_width = size(src_img, 2);
    src_channels = size(src_img, 3);
    dest_width = dest_canvas_width_height(1);
	dest_height	= dest_canvas_width_height(2);
    
    result_img = zeros(dest_height, dest_width, src_channels);
    mask = false(dest_height, dest_width);
    
    % this is the overall region covered by result_img
    [dest_X, dest_Y] = meshgrid(1:dest_width, 1:dest_height);
    
    % map result_img region to src_img coordinate system using the given
    % homography.
    src_pts = applyHomography(resultToSrc_H, [dest_X(:), dest_Y(:)]);
    src_X = reshape(src_pts(:,1), dest_height, dest_width);
    src_Y = reshape(src_pts(:,2), dest_height, dest_width);
    
    % ---------------------------
    % START ADDING YOUR CODE HERE
    % ---------------------------
    
    % Set 'mask' to the correct values based on src_pts.
    
    % fill the right region in 'result_img' with the src_img
    R_channel = interp2(1 : src_width, 1 : src_height, src_img(:, :, 1), src_pts(:, 1), src_pts(:, 2));
    G_channel = interp2(1 : src_width, 1 : src_height, src_img(:, :, 2), src_pts(:, 1), src_pts(:, 2));
    B_channel = interp2(1 : src_width, 1 : src_height,src_img(:, :, 3), src_pts(:, 1), src_pts(:, 2));
    
    add=[R_channel G_channel B_channel];
    result_img=reshape(add,[dest_height,dest_width,3]);
   
    result_img(isnan(result_img)) = 0;
    
    src_points = [1, 1; 1, src_height; src_width, src_height; src_width, 1; 1, 1];
    dest_points = applyHomography(inv(resultToSrc_H),src_points);
    
    mask = im2double(poly2mask(dest_points(:, 1), dest_points(:, 2), dest_height, dest_width));

    result_img = result_img .* cat(3, mask, mask, mask);

    
    % ---------------------------
    % END YOUR CODE HERE    
    % ---------------------------
    
    
end