export const mockMeals = [
  {
    id: 1,
    budgetCategories: [20, 100],
    diet: 'Veg',
    target: 'Adult',
    name: "Poha + Peanut + Small Banana",
    cost: 45,
    matchScore: 92,
    badges: ["Best Value", "Budget Fit", "Quick Prep"],
    nutrients: {
      protein: { value: 12, percent: 48 },
      carbs: { value: 65, percent: 92 },
      fat: { value: 14, percent: 93 },
      calories: 320
    },
    story: [
      { id: "s1", icon: "protein", title: "12g protein (48% need)", text: "Efficient plant-based fuel" },
      { id: "s2", icon: "health", title: "Supports iron levels", text: "Covers daily baseline" },
      { id: "s3", icon: "goal", title: "Improves focus & energy", text: "Sustained carbohydrate release" }
    ],
    deficiencies: {
      confidence: 88,
      text: "Symptoms common to low Vitamin B12 and iron. Meal combats iron deficiency."
    }
  },
  {
    id: 2,
    budgetCategories: [30, 100],
    diet: 'Veg',
    target: 'Adult',
    name: "Moong Dal Chilla + Curd",
    cost: 55,
    matchScore: 96,
    badges: ["High Protein", "Cheapest", "Low GI"],
    nutrients: {
      protein: { value: 18, percent: 72 },
      carbs: { value: 40, percent: 65 },
      fat: { value: 12, percent: 80 },
      calories: 280
    },
    story: [
      { id: "s1", icon: "protein", title: "18g clean protein", text: "Excellent vegetarian source" },
      { id: "s2", icon: "health", title: "Probiotic rich", text: "Improves gut health" },
      { id: "s3", icon: "goal", title: "Low glycemic index", text: "Prevents sugar spikes" }
    ],
    deficiencies: {
      confidence: 91,
      text: "Addresses potential calcium gaps based on input profile."
    }
  },
  {
    id: 3,
    budgetCategories: [50, 200],
    diet: 'Non-Veg',
    target: 'Adult',
    name: "3 Eggs + Oats Bowl + Apple",
    cost: 110,
    matchScore: 98,
    badges: ["Premium Quality", "Best Value", "Balanced"],
    nutrients: {
      protein: { value: 24, percent: 96 },
      carbs: { value: 45, percent: 90 },
      fat: { value: 18, percent: 90 },
      calories: 410
    },
    story: [
      { id: "s1", icon: "protein", title: "24g premium protein", text: "Complete amino acid profile" },
      { id: "s2", icon: "health", title: "Rich in Vitamin D", text: "Supports bone density" },
      { id: "s3", icon: "goal", title: "Morning focus", text: "Optimal for brain function" }
    ],
    deficiencies: {
      confidence: 94,
      text: "Addresses potential Vitamin D and healthy fat gaps."
    }
  },
  {
    id: 4,
    budgetCategories: [60, 200],
    diet: 'Non-Veg',
    target: 'Adult',
    name: "Chicken Sandwich + Milk",
    cost: 130,
    matchScore: 95,
    badges: ["High Protein", "Quick Prep"],
    nutrients: {
      protein: { value: 32, percent: 100 },
      carbs: { value: 35, percent: 70 },
      fat: { value: 16, percent: 85 },
      calories: 450
    },
    story: [
      { id: "s1", icon: "protein", title: "32g massive protein", text: "Optimal for muscle recovery" },
      { id: "s2", icon: "health", title: "Calcium enriched", text: "Strengthens bones" },
      { id: "s3", icon: "goal", title: "Sustained satiation", text: "Keeps you full longer" }
    ],
    deficiencies: {
      confidence: 92,
      text: "Great for building lean muscle mass and structural strength."
    }
  },
  {
    id: 5,
    budgetCategories: [150, 500],
    diet: 'Veg',
    target: 'Adult',
    name: "Paneer Tikka Salad Bowl",
    cost: 180,
    matchScore: 99,
    badges: ["Premium Quality", "Keto Friendly"],
    nutrients: {
      protein: { value: 28, percent: 100 },
      carbs: { value: 20, percent: 40 },
      fat: { value: 25, percent: 95 },
      calories: 390
    },
    story: [
      { id: "s1", icon: "protein", title: "28g dairy protein", text: "Slow digesting casein" },
      { id: "s2", icon: "health", title: "High healthy fats", text: "Supports hormonal balance" },
      { id: "s3", icon: "goal", title: "Low carbohydrate", text: "Perfect for cutting phases" }
    ],
    deficiencies: {
      confidence: 96,
      text: "Covers all major fat-soluble vitamins."
    }
  },
  {
    id: 6,
    budgetCategories: [150, 500],
    diet: 'Non-Veg',
    target: 'Adult',
    name: "Grilled Fish + Quinoa",
    cost: 280,
    matchScore: 99,
    badges: ["Super Premium", "Omega-3 Rich"],
    nutrients: {
      protein: { value: 42, percent: 100 },
      carbs: { value: 30, percent: 60 },
      fat: { value: 18, percent: 90 },
      calories: 420
    },
    story: [
      { id: "s1", icon: "protein", title: "42g lean protein", text: "Maximum muscle synthesis" },
      { id: "s2", icon: "health", title: "High EPA/DHA Omega-3", text: "Brain & heart health protection" },
      { id: "s3", icon: "goal", title: "Zero inflammation", text: "Cleanest energy source" }
    ],
    deficiencies: {
      confidence: 98,
      text: "Maximum micronutrient coverage. Zero vitamin deficiencies predicted."
    }
  }
];

export const getMealsOptions = (budget, dietFilter) => {
  // Return top 3 meals that match budget and diet, if not enough, loosen budget
  let results = mockMeals.filter(m => budget >= m.budgetCategories[0] && budget <= m.budgetCategories[1]);
  if (dietFilter !== 'All') {
    results = results.filter(m => m.diet === dietFilter || m.target === dietFilter);
  }
  
  if (results.length === 0) {
    return [mockMeals[0], mockMeals[1]]; // fallback
  }
  
  return results.slice(0, 3);
};
