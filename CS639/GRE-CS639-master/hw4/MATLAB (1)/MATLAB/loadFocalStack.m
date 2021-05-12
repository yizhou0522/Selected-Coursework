function [rgb_stack, gray_stack] = loadFocalStack(focal_stack_dir)
  files = dir(fullfile(focal_stack_dir, '*.jpg'));
    file_names = {files.name};
    N=length(files);
    img= size(imread(fullfile(focal_stack_dir, file_names{1})));
    rgb_stack = uint8(zeros(img(1), img(2), 3 * N));
    gray_stack = uint8(zeros(img(1), img(2), N)); 
    for i = 1 : N
        new_idx=(i-1)*3+1;
        rgb_stack(:, :, new_idx : new_idx+2) = imread(fullfile(focal_stack_dir, file_names{i}));
        gray_stack(:, :, i) = rgb2gray(imread(fullfile(focal_stack_dir, file_names{i})));
    end
end
