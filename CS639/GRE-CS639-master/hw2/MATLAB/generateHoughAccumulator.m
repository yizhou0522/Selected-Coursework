function hough_img = generateHoughAccumulator(img, theta_num_bins, rho_num_bins)  
    diag_num=800;
    rho_step=(2 * diag_num + 1)/rho_num_bins;
    [rows, cols] = size(img);
    max_rho = sqrt(rows^2 + cols^2);
    count = 2 * rho_num_bins + 1;
    hough_img = zeros(theta_num_bins, count);
    theta_step=180 / theta_num_bins;
    for i = 1 : rows
        for j = 1 : cols
            if img(i, j) ~= 0
                for index1 = 1 : theta_num_bins
                    theta = index1 * theta_step;
                    rho = j * cosd(theta) - i * sind(theta); 
                    % x sin(theta) – y cos(theta) + roh = 0 .
                    index2 = round(rho * rho_num_bins / max_rho + rho_num_bins);
                    if index2 > 0 && index2 <= count
                        hough_img(index1, index2) = hough_img(index1, index2) + 1;
                        rho_step=rho_step+round(theta_step*index2)+1;
                    end                   
                end           
            else
                rho_step=(2 * diag_num + 1)/rho_num_bins;
            end
        end
    end
    min_num = min(hough_img(:));
    max_num = max(hough_img(:));
    for i = 1 : theta_num_bins
        for j = 1 : count
            x=hough_img(i, j) - min_num;
            y=(max_num - min_num);
            hough_img(i, j) = ceil(x/y*255);
        end
    end
end



