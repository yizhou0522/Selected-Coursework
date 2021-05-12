images = load_and_center_dataset('mnist.npy')
    s = get_covariance(images)
    Lambda, U = get_eig(s, 20) 
    #diagonal matrix for eigenValues; corresponding first 20 eigenVector
    image = images[19] # third index image in the dataset
    image_proj = project_image(image, U) # project the image
    # print(image_proj)
    display_image(image, image_proj)