//
//  FetchModel.swift
//  FetchTest
//
//  Created by 田贊拓 on 2023/5/30.
//

import Foundation

class FetchModel: ObservableObject{
    @Published var meal = [Meal]()
    @Published var detail = [Detail]()
    var idMeal = 0
    
    init(){
        getDesertData()
        for i in 0..<self.meal[0].meals.count{
            getDetailData(name: self.meal[0].meals[i].idMeal)
        }
    }
    
    func getDesertData(){
        let url = Bundle.main.url(forResource: "dessert", withExtension: "json")
        do{
            let data = try Data(contentsOf: url!)
            let decoder = JSONDecoder()
            let mealData = try decoder.decode(Meal.self, from: data)
            self.meal.append(mealData)
        }catch{
            print(error)
        }
    }
    
    func getDetailData(name: String){
        let url = URL(string: "https://felixtien.github.io/Data/\(name).json")
        guard url != nil else{
            return
        }
        let request = URLRequest(url: url!)
        let session = URLSession.shared
        let dataTask = session.dataTask(with: request) { data, response, error in
            guard error == nil else{
                return
            }
            let decoder = JSONDecoder()
            do{
                let detailData = try decoder.decode(Detail.self, from: data!)
                DispatchQueue.main.async {
                    self.detail.append(detailData)
                }
            }catch{
                print(error)
            }
        }
        dataTask.resume()
    }
    
    func idMealFind(id: String) -> Int{
        for i in 0..<self.detail.count{
            if id == self.detail[i].meals[0].idMeal{
                return i
            }
        }
        return 0
    }
}
