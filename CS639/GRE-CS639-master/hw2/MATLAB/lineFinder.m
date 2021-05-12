function line_detected_img = lineFinder(orig_img, hough_img, hough_threshold)
    theta_num_bins=size(hough_img,2);
    theta_step = 180/theta_num_bins;
    [rows, cols] = size(orig_img);
    max_rho = sqrt(rows^2 + cols^2);
    [theta_val, rho_val] = size(hough_img);
    fh = figure; 
    imshow(orig_img);
    hold on;    
    for i = 1 : theta_val
        for j = 1 : rho_val
            if hough_img(i, j) > hough_threshold
                theta = i * 180 / theta_val;
                rho = max_rho * (j / (rho_val / 2) - 1);
                x = 1 : rows;
                y = x * sind(theta)/cosd(theta) + rho /cosd(theta);
                line(y,x, 'LineWidth',2, 'Color', [0, 0, 1]);
                theta_step=theta_step+1;
            end 
        end
    end
    line_detected_img = saveAnnotatedImg(fh);
end

function annotated_img = saveAnnotatedImg(fh)
    figure(fh); % Shift the focus back to the figure fh

    % The figure needs to be undocked
    set(fh, 'WindowStyle', 'normal');

    % The following two lines just to make the figure true size to the
    % displayed image. The reason will become clear later.
    img = getimage(fh);
    truesize(fh, [size(img, 1), size(img, 2)]);

    % getframe does a screen capture of the figure window, as a result, the
    % displayed figure has to be in true size. 
    frame = getframe(fh);
    frame = getframe(fh);
    pause(0.5); 
    % Because getframe tries to perform a screen capture. it somehow 
    % has some platform depend issues. we should calling
    % getframe twice in a row and adding a pause afterwards make getframe work
    % as expected. This is just a walkaround. 
    annotated_img = frame.cdata;
end