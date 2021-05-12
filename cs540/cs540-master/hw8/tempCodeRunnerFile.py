 print(get_dataset('bodyfat.csv'))
    print_stats(dataset, 1)
    print(regression(dataset, cols=[2,3], betas=[0,0,0]))
    print(regression(dataset, cols=[2,3,4], betas=[0,-1.1,-.2,3]))
    print(gradient_descent(dataset, cols=[2,3], betas=[0,0,0]))
    iterate_gradient(dataset, cols=[1,8], betas=[400,-400,300], T=10, eta=1e-4)
    print(compute_betas(dataset, cols=[1,2]))
    print(predict(dataset, cols=[1,2], features=[1.0708, 23]))
    sgd(dataset, cols=[2,8], betas=[-4