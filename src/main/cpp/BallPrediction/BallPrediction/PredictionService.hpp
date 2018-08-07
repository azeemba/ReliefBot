#pragma once

#include "chip\inc\vec.h"
#include "chip\inc\Ball.h"
#include <list>

struct BallSlice {
	vec3 Location;
	vec3 Velocity;
	vec3 AngularVelocity;
	float gameSeconds;
};


class PredictionService {
private:
	const float secondsToPredict;
	const float stepInterval;
	const int expectedNumSlices;
	std::list<BallSlice> prediction;
	bool currentPredictionStillValid(BallSlice currentBallPosition);
	bool expectedBallPosition(float gameSeconds, BallSlice* outputSlice);
	Ball ball;
	void makePrediction(BallSlice ballSlice, std::list<BallSlice>* predictionOut);
public:
	PredictionService(float secondsToPredict, float stepInterval) : 
		prediction(), 
		secondsToPredict(secondsToPredict), 
		stepInterval(stepInterval),
		expectedNumSlices(secondsToPredict / stepInterval),
		ball()
	{ 
	}
	std::list<BallSlice>* updatePrediction(BallSlice slice);
	void makeFreshPrediction(BallSlice ballSlice, std::list<BallSlice>* predictionOut);
};
