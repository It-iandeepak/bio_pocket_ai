import React, { useState, useEffect } from 'react';
import { getMealsOptions } from './data';
import {
  Sparkles, ShieldCheck, Flame, Activity, ChevronDown, CheckCircle, ChevronLeft,
  TrendingUp, Mic, X, Scale
} from 'lucide-react';

const Nav = ({ view, onBack }) => (
  <nav className="top-nav">
    <div className="logo">
      <Activity className="logo-icon" size={28} color="#10b981" />
      BioPocket
    </div>
    {view === 'result' && (
      <button className="btn-secondary" style={{ padding: '0.4rem 0.8rem', border: 'none' }} onClick={onBack}>
        <ChevronLeft size={18} /> Back
      </button>
    )}
  </nav>
);

const Hero = ({ onGenerate }) => {
  const [mode, setMode] = useState('budget'); // 'budget' | 'nutrition'
  const [budget, setBudget] = useState(50);
  const [goal, setGoal] = useState("High Protein");
  const [isRecording, setIsRecording] = useState(false);

  const getHint = (val) => {
    if (val < 50) return "Basic sustenance";
    if (val < 100) return "Balanced meal";
    if (val < 150) return "High protein zone";
    return "Premium quality meal";
  };

  const toggleRecording = () => {
    setIsRecording(true);
    setTimeout(() => {
      setIsRecording(false);
      setGoal("Find me a high protein meal under ₹100");
      setBudget(100);
    }, 2000); // Mock voice recording animation
  };

  return (
    <div className="hero-screen">
      <div className="hero-content">
        <h1 className="hero-title">Optimize Your Nutrition intuitively</h1>
        <p className="hero-subtext">The smartest way to get the exact nutrients you need, completely tailored to your limits.</p>

        {/* Mode Toggle */}
        <div className="mode-toggle" style={{ marginLeft: 0, marginTop: '2rem' }}>
          <button className={`mode-btn ${mode === 'budget' ? 'active' : ''}`} onClick={() => setMode('budget')}>
            💵 Budget
          </button>
          <button className={`mode-btn ${mode === 'nutrition' ? 'active' : ''}`} onClick={() => setMode('nutrition')}>
            🎯 Nutrition
          </button>
        </div>
      </div>

      <div className="hero-card">
        <div className="slider-container glass">
          {mode === 'budget' ? (
            <>
              <div className="input-header">
                <div style={{ fontSize:'0.85rem', fontWeight: 700, color: '#6b7280', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Your Limit</div>
                <button className={`mic-btn ${isRecording ? 'recording' : ''}`} onClick={toggleRecording}>
                  <Mic size={18} />
                </button>
              </div>
              <div className="budget-value-row">
                <span className="budget-currency">₹</span>
                <span className="budget-value">{budget}</span>
              </div>
              <div className="budget-hint">{getHint(budget)}</div>
              
              <input type="range" min="20" max="500" value={budget} onChange={(e) => setBudget(Number(e.target.value))} className="slider" />
              <div className="slider-track" style={{ 
                position: 'absolute', height: '10px', background: '#10b981', borderRadius: '5px', pointerEvents: 'none',
                top: 'calc(100% - 2.5rem)', left: '1.5rem', width: `calc(${((budget - 20) / 480) * 100}% - 36px/2)`
              }}></div>
            </>
          ) : (
            <>
              <div className="input-header">
                <div style={{ fontSize:'0.85rem', fontWeight: 700, color: '#6b7280', textTransform: 'uppercase' }}>Nutrition Goal</div>
                <button className={`mic-btn ${isRecording ? 'recording' : ''}`} onClick={toggleRecording}>
                  <Mic size={18} />
                </button>
              </div>
              <input 
                type="text" 
                value={goal}
                onChange={(e) => setGoal(e.target.value)}
                style={{ width: '100%', padding: '1rem', marginTop: '1rem', fontSize: '1.1rem', borderRadius: '12px', border: '1px solid #e5e7eb', outline: 'none', fontWeight: 600 }}
                placeholder="E.g., High iron meal"
              />
              <div className="budget-hint" style={{ marginTop: '1rem' }}>Or try: "Low carb", "Vegan protein"</div>
            </>
          )}
        </div>

        <div className="sticky-cta" style={{ position: 'static', padding: 0, background: 'transparent' }}>
          <button className="btn-primary btn-pulse" onClick={() => onGenerate(budget)}>
            <Sparkles size={22} fill="currentColor" /> ⚡ Optimize My Meal
          </button>
        </div>
      </div>
    </div>
  );
};

const Loading = () => (
  <div className="loading-screen">
    <div className="skeleton-title"></div>
    <div className="skeleton-card"></div>
    <div className="skeleton-card" style={{ minHeight: '120px', animationDelay: '0.2s', width: '80%' }}></div>
    <div className="loading-text">Optimizing your meal...</div>
  </div>
);

const Accordion = ({ title, icon: Icon, children }) => {
  const [open, setOpen] = useState(false);
  return (
    <div className={`accordion ${open ? 'open' : ''}`}>
      <div className="accordion-header" onClick={() => setOpen(!open)}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
          {Icon && <Icon size={18} className="icon" />}
          <span>{title}</span>
        </div>
        <ChevronDown size={20} className="icon" />
      </div>
      <div className="accordion-content">
        <div className="accordion-inner">{children}</div>
      </div>
    </div>
  );
};

const Result = ({ initialBudget, onBack }) => {
  const [budget, setBudget] = useState(initialBudget);
  const [dietFilter, setDietFilter] = useState('All');
  const filters = ['All', 'Veg', 'Non-Veg', 'Adult', 'Child', 'Patient'];
  
  const [meals, setMeals] = useState([]);
  const [activeMealIndex, setActiveMealIndex] = useState(0);
  const [isComparing, setIsComparing] = useState(false);

  useEffect(() => {
    const fetched = getMealsOptions(budget, dietFilter);
    setMeals(fetched);
    setActiveMealIndex(0);
  }, [budget, dietFilter]);

  if (!meals.length) return <div style={{padding: '2rem', textAlign: 'center'}}>No meals found</div>;

  const currentMeal = meals[activeMealIndex] || meals[0];

  return (
    <div className="result-screen">
      
      {/* Category Chips - Zomato Style */}
      <div className="chips-container">
        {filters.map(f => (
          <div key={f} className={`chip ${dietFilter === f ? 'active' : ''}`} onClick={() => setDietFilter(f)}>
            {f === 'Veg' ? '🌱 Veg' : f === 'Non-Veg' ? '🍗 Non-Veg' : f === 'Child' ? '👶 Child' : f === 'Adult' ? '🧑 Adult' : f === 'Patient' ? '🏥 Patient' : '✨ All'}
          </div>
        ))}
      </div>

      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
        <div className="trust-badge">
          <Activity size={14} /> AI Confidence: {currentMeal.deficiencies.confidence}%
        </div>
        {meals.length > 1 && (
          <button className="btn-compare" onClick={() => setIsComparing(true)} style={{ marginBottom: '1.5rem'}}>
            <Scale size={14} style={{ display: 'inline', marginRight: '4px', verticalAlign: 'middle' }}/>
            Compare
          </button>
        )}
      </div>

      {/* Swipeable Meal Cards */}
      <div className="meals-scroll">
        {meals.map((meal, index) => (
          <div className="meal-card-wrap" key={meal.id} onClick={() => setActiveMealIndex(index)}>
            <div className="meal-card" style={{ 
              transform: index === activeMealIndex ? 'scale(1)' : 'scale(0.96)', 
              border: index === activeMealIndex ? '2px solid #10b981' : '1px solid #e5e7eb', 
              opacity: index === activeMealIndex ? 1 : 0.7 
            }}>
              <div className="badges-row" style={{ display: 'flex', gap: '0.5rem', marginBottom: '1rem', flexWrap: 'wrap' }}>
                {meal.badges.map((b, i) => (
                  <span key={i} className={`badge ${i === 0 ? 'badge-value' : 'badge-budget'}`}>
                    {i === 0 && <CheckCircle size={10} />} {b}
                  </span>
                ))}
              </div>
              
              <div className="card-header">
                <div className="meal-name">{meal.name}</div>
                <div className="meal-cost"><span className="meal-cost-currency">₹</span>{meal.cost}</div>
              </div>

              <div className="macro-blocks">
                <div className="macro-block"><div className="macro-val val-protein">{meal.nutrients.protein.value}g</div><div className="macro-label">Protein</div></div>
                <div className="macro-block"><div className="macro-val val-carbs">{meal.nutrients.carbs.value}g</div><div className="macro-label">Carbs</div></div>
                <div className="macro-block"><div className="macro-val val-fat">{meal.nutrients.fat.value}g</div><div className="macro-label">Fat</div></div>
              </div>
            </div>
          </div>
        ))}
      </div>

      <div className="details-section">
        {/* Storytelling Section */}
        <div>
          <h3 style={{ fontSize: '1.25rem', fontWeight: 800, marginBottom: '1.25rem', color: '#111827' }}>Why this meal?</h3>
          <div className="story-grid">
            {currentMeal.story.map((s) => (
              <div className="story-item" key={s.id}>
                <div className={`story-icon icon-${s.icon}`}>
                  {s.icon === 'protein' && <Flame size={24} />}
                  {s.icon === 'health' && <ShieldCheck size={24} />}
                  {s.icon === 'goal' && <TrendingUp size={24} />}
                </div>
                <div className="story-content">
                  <h4>{s.title}</h4>
                  <p>{s.text}</p>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Progressive Disclosure */}
        <div style={{ display: 'flex', flexDirection: 'column'}}>
          <h3 style={{ fontSize: '1.25rem', fontWeight: 800, marginBottom: '1.25rem', color: '#111827', visibility: 'hidden' }}>Insights</h3>
          <div className="accordions-container">
            <Accordion title="Check Deficiencies" icon={Activity}>
              <p style={{ fontWeight: 600, color: '#111827', marginBottom: '0.5rem' }}>AI Health Analysis</p>
              <p>{currentMeal.deficiencies.text}</p>
            </Accordion>
          </div>

          <div className="sticky-cta">
            <button className="btn-primary" onClick={onBack}>
              <ChevronLeft size={20} /> Optimise Another Meal
            </button>
          </div>
        </div>
      </div>

      {/* Comparison Modal */}
      {isComparing && meals.length >= 2 && (
        <div className="modal-overlay">
          <div className="modal-content">
            <div className="modal-header">
              <h3>Compare Meals</h3>
              <button className="close-btn" onClick={() => setIsComparing(false)}><X size={18} /></button>
            </div>
            <div className="compare-grid">
              <div className="compare-col">
                <div className="compare-title">{meals[0].name}</div>
                <div className="compare-row"><span className="c-label">Cost</span><span className={`c-val ${meals[0].cost <= meals[1].cost ? 'win' : ''}`}>₹{meals[0].cost}</span></div>
                <div className="compare-row"><span className="c-label">Protein</span><span className={`c-val ${meals[0].nutrients.protein.value >= meals[1].nutrients.protein.value ? 'win' : ''}`}>{meals[0].nutrients.protein.value}g</span></div>
                <div className="compare-row"><span className="c-label">Calories</span><span className="c-val">{meals[0].nutrients.calories} kcal</span></div>
              </div>
              <div className="compare-col">
                <div className="compare-title">{meals[1].name}</div>
                <div className="compare-row"><span className="c-label">Cost</span><span className={`c-val ${meals[1].cost <= meals[0].cost ? 'win' : ''}`}>₹{meals[1].cost}</span></div>
                <div className="compare-row"><span className="c-label">Protein</span><span className={`c-val ${meals[1].nutrients.protein.value >= meals[0].nutrients.protein.value ? 'win' : ''}`}>{meals[1].nutrients.protein.value}g</span></div>
                <div className="compare-row"><span className="c-label">Calories</span><span className="c-val">{meals[1].nutrients.calories} kcal</span></div>
              </div>
            </div>
          </div>
        </div>
      )}

    </div>
  );
};

export default function App() {
  const [budget, setBudget] = useState(50);
  const [view, setView] = useState('hero'); // 'hero', 'loading', 'result'

  const handleGenerate = (finalBudget) => {
    setBudget(finalBudget);
    setView('loading');
    setTimeout(() => {
      setView('result');
    }, 1500);
  };

  return (
    <div className="app-container">
      <Nav view={view} onBack={() => setView('hero')} />
      {view === 'hero' && <Hero onGenerate={handleGenerate} />}
      {view === 'loading' && <Loading />}
      {view === 'result' && <Result initialBudget={budget} onBack={() => setView('hero')} />}
    </div>
  );
}
