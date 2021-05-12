function trackingTester(data_params, tracking_params)
    % Useful function to get ROI from the img 
    function roi = get_ROI(img, rect)
        % just a convenience function
        xmin = rect(1);
        ymin = rect(2);
        width = rect(3);
        height = rect(4);
        roi = img(ymin:ymin+height-1, xmin:xmin+width-1,:);
    end
    
    % Verify that output directory exists
    if ~exist(data_params.out_dir, 'dir')
        fprintf(1, "Creating directory %s.\n", data_params.out_dir);
        mkdir(data_params.out_dir);
    end
    trackingbox_color = [255, 255, 0];
    % Load the first frame, draw a box on top of that frame, and save it.
    first_frame = imread(fullfile(data_params.data_dir, data_params.genFname(1)));
    annotated_first_frame = drawBox(first_frame, tracking_params.rect, trackingbox_color, 3);
    imwrite(annotated_first_frame, fullfile(data_params.out_dir, data_params.genFname(1)));
    
    % take the ROI from the first frame and keep its histogram to match
    % later
    obj_roi = get_ROI(first_frame, tracking_params.rect);
    %------------- FILL IN CODE -----------------
    % Hint: Use rgb2ind function to transform the image such that it only has a
    % fixed number of colors. If you visualize the template image it
    % should look similar to the obj_roi image, but with much less
    % variations in the color. The output colormap will tell you which
    % colors were chosen to be used in the template output image.
    % Create a color histogram from the template image that has the colors
    % quantized.
    % Hint: If the template image has Q different colors, then your
    % histogram will have Q bins, one for each color.
    % Normalize histogram such that its sum = 1
    rect_src = tracking_params.rect;
    [mapped_obj, colormap] = rgb2ind(obj_roi, tracking_params.bin_n);
    [obj_hist, ~] = histcounts(mapped_obj, tracking_params.bin_n);
    obj_hist = double(obj_hist) / sum(obj_hist(:));
    %------------- END FILL IN CODE -----------------

    % Tracking loop
    % -------------    
    % initial location of tracking box
    obj_col = tracking_params.rect(1);
    obj_row = tracking_params.rect(2);
    obj_width = tracking_params.rect(3);
    obj_height = tracking_params.rect(4);
    frame_ids = data_params.frame_ids;
    for frame_id = frame_ids(2:end)
        % Read current frame
        fprintf('On frame %d\n', frame_id);
        frame = imread(fullfile(data_params.data_dir, data_params.genFname(frame_id)));
        %------------- FILL IN CODE -----------------
        % extract the area over which we will search for the object
        % Hint:  This step is very similar to what you did in computeFlow
        % to extract the search_area.
        xmin = max(1, obj_row-tracking_params.search_radius);
        xmax = min(size(frame,1), obj_row+obj_height-1+tracking_params.search_radius);
        ymin = max(1, obj_col-tracking_params.search_radius);
        ymax = min(size(frame,2), obj_col+obj_width-1+tracking_params.search_radius);
        search_window = frame(xmin:xmax,ymin:ymax,:);
        gray_search_window = rgb2ind(search_window, colormap);
        %------------- END FILL IN CODE -----------------

        % extract each object-sized sub-region from the searched area and
        % make it a column
        candidate_windows = im2col(gray_search_window, [obj_height obj_width], 'sliding');
        num_windows = size(candidate_windows, 2);
        % compute histograms for each candidate sub-region extracted from
        % the search window
        candidate_hists = double(zeros(tracking_params.bin_n, num_windows));
        for i = 1:num_windows
            %------------- FILL IN CODE -----------------
            % Hint: You already have done this at the beginning of this
            % function.
            candidate_hists(:,i) = histcounts(candidate_windows(:, i), tracking_params.bin_n);          
            % Normalize histogram such that its sum = 1
            candidate_hists(:,i) = candidate_hists(:,i) / sum(candidate_hists(:,i));
            %------------- END FILL IN CODE -----------------
        end
        
        %------------- FILL IN CODE -----------------
        
        % find the best-matching region
        % Hint: You have all the candidate histograms, and you want to find
        % the one that is the most similar to the histogram you computed
        % from the first frame       
        % UPDATE the obj_row and obj_col, which tell us the location of the
        % top-left pixel of the bounding box around the object we are
        % tracking.
        [~, min_idx] = min(sum((candidate_hists - obj_hist').^2));
        input1=size(search_window, 1);
        input2=size(search_window, 2);
        [offset1, offset2] = ind2sub([input1 input2 ] - [obj_height obj_width] + 1, min_idx);
        obj_row = xmin + offset1;
        obj_col = ymin + offset2;
        %------------- END FILL IN CODE -----------------

        % generate box annotation for the current frame
        annotated_frame = drawBox(frame, [obj_col obj_row obj_width obj_height], trackingbox_color, 3);
        % save annotated frame in the output directory
        imwrite(annotated_frame, fullfile(data_params.out_dir, data_params.genFname(frame_id)));
    end
end
